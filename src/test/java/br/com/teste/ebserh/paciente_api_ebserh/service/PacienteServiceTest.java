package br.com.teste.ebserh.paciente_api_ebserh.service;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import br.com.teste.ebserh.paciente_api_ebserh.exception.CpfJaCadastradoException;
import br.com.teste.ebserh.paciente_api_ebserh.exception.RegraNegocioException;
import br.com.teste.ebserh.paciente_api_ebserh.mapper.PacienteMapper;
import br.com.teste.ebserh.paciente_api_ebserh.repository.PacienteRepository;
import br.com.teste.ebserh.paciente_api_ebserh.entity.Paciente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @Mock
    private PacienteMapper mapper;

    @InjectMocks
    private PacienteService service;


    @Test
    void deveCriarPacienteComSucesso() {

        CriarPacienteRequest request = criarRequestValido();

        Paciente paciente = new Paciente();


        when(repository.existsByCpf(request.getCpf()))
                .thenReturn(false);

        when(mapper.toEntity(request))
                .thenReturn(paciente);

        when(repository.save(paciente))
                .thenReturn(paciente);


        service.criarPaciente(request);


        verify(repository, times(1))
                .save(paciente);
    }


    @Test
    void naoDeveCriarPacienteComCpfDuplicado() {

        CriarPacienteRequest request = criarRequestValido();


        when(repository.existsByCpf(request.getCpf()))
                .thenReturn(true);


        assertThrows(
                CpfJaCadastradoException.class,
                () -> service.criarPaciente(request)
        );


        verify(repository, never())
                .save(any());
    }


    @Test
    void naoDevePermitirPacienteInternadoSemAmbiente() {

        CriarPacienteRequest request = criarRequestValido();

        request.setAmbienteInternacao(null);


        when(repository.existsByCpf(request.getCpf()))
                .thenReturn(false);


        assertThrows(
                RegraNegocioException.class,
                () -> service.criarPaciente(request)
        );


        verify(repository, never())
                .save(any());
    }


    @Test
    void naoDevePermitirPacienteInternadoSemLeito() {

        CriarPacienteRequest request = criarRequestValido();

        request.setNumeroLeito(null);


        when(repository.existsByCpf(request.getCpf()))
                .thenReturn(false);


        assertThrows(
                RegraNegocioException.class,
                () -> service.criarPaciente(request)
        );


        verify(repository, never())
                .save(any());
    }


    @Test
    void naoDevePermitirPacienteNaoInternadoComLeito() {

        CriarPacienteRequest request = criarRequestValido();

        request.setInternado(false);
        request.setAmbienteInternacao(null);
        request.setNumeroLeito(10);


        when(repository.existsByCpf(request.getCpf()))
                .thenReturn(false);


        assertThrows(
                RegraNegocioException.class,
                () -> service.criarPaciente(request)
        );


        verify(repository, never())
                .save(any());
    }


    private CriarPacienteRequest criarRequestValido() {

        CriarPacienteRequest request = new CriarPacienteRequest();

        request.setNome("João da Silva");
        request.setCpf("52998224725");
        request.setDataNascimento(
                LocalDate.of(1995, 5, 10)
        );
        request.setTipoSanguineo(
                TipoSanguineo.A_POSITIVO
        );
        request.setInternado(true);
        request.setAmbienteInternacao(
                AmbienteInternacao.UTI
        );
        request.setNumeroLeito(10);

        return request;
    }

}