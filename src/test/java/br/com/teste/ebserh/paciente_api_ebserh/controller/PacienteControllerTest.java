package br.com.teste.ebserh.paciente_api_ebserh.controller;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.response.PacienteResponse;
import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import br.com.teste.ebserh.paciente_api_ebserh.exception.CpfJaCadastradoException;
import br.com.teste.ebserh.paciente_api_ebserh.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PacienteController.class)
@Import(br.com.teste.ebserh.paciente_api_ebserh.exception.GlobalExceptionHandler.class)
class PacienteControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private PacienteService service;


    @Test
    void deveCriarPacienteComSucesso() throws Exception {

        PacienteResponse response = PacienteResponse.builder()
                .id(UUID.randomUUID())
                .nome("João Silva")
                .cpf("52998224725")
                .build();


        when(service.criarPaciente(any(CriarPacienteRequest.class)))
                .thenReturn(response);


        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                criarRequestValido()
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome")
                        .value("João Silva"));

    }


    @Test
    void naoDeveCriarPacienteComNomeVazio() throws Exception {

        CriarPacienteRequest request = criarRequestValido();

        request.setNome("");


        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }


    @Test
    void naoDeveCriarPacienteComCpfDuplicado() throws Exception {


        when(service.criarPaciente(any()))
                .thenThrow(new CpfJaCadastradoException());


        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                criarRequestValido()
                        )))
                .andExpect(status().isConflict());

    }


    @Test
    void deveBuscarPacientePorId() throws Exception {

        UUID id = UUID.randomUUID();


        PacienteResponse response =
                PacienteResponse.builder()
                        .id(id)
                        .nome("Maria")
                        .build();


        when(service.buscarPorId(id))
                .thenReturn(response);


        mockMvc.perform(get("/pacientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome")
                        .value("Maria"));

    }


    @Test
    void deveExcluirPaciente() throws Exception {

        UUID id = UUID.randomUUID();


        mockMvc.perform(delete("/pacientes/{id}", id))
                .andExpect(status().isNoContent());

    }


    private CriarPacienteRequest criarRequestValido() {

        CriarPacienteRequest request =
                new CriarPacienteRequest();

        request.setNome("João Silva");
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