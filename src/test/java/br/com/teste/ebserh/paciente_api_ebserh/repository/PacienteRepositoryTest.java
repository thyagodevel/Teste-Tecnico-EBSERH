package br.com.teste.ebserh.paciente_api_ebserh.repository;

import br.com.teste.ebserh.paciente_api_ebserh.entity.Paciente;
import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository repository;


    @Test
    void deveSalvarPacienteComSucesso() {

        Paciente paciente = criarPaciente();


        Paciente salvo = repository.save(paciente);


        assertNotNull(salvo.getId());
        assertEquals("João da Silva", salvo.getNome());
        assertEquals("52998224725", salvo.getCpf());
    }


    @Test
    void deveEncontrarPacientePorCpf() {

        Paciente paciente = criarPaciente();

        repository.save(paciente);


        var encontrado = repository.findByCpf(
                "52998224725"
        );


        assertTrue(encontrado.isPresent());
        assertEquals(
                "João da Silva",
                encontrado.get().getNome()
        );
    }


    @Test
    void deveVerificarCpfExistente() {

        Paciente paciente = criarPaciente();

        repository.save(paciente);


        boolean existe = repository.existsByCpf(
                "52998224725"
        );


        assertTrue(existe);
    }


    private Paciente criarPaciente() {

        Paciente paciente = new Paciente();

        paciente.setNome("João da Silva");
        paciente.setCpf("52998224725");
        paciente.setDataNascimento(
                LocalDate.of(1995, 5, 10)
        );
        paciente.setTipoSanguineo(
                TipoSanguineo.A_POSITIVO
        );
        paciente.setInternado(true);
        paciente.setAmbienteInternacao(
                AmbienteInternacao.UTI
        );
        paciente.setNumeroLeito(10);

        return paciente;
    }

}