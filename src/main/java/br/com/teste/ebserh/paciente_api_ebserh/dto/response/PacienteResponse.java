package br.com.teste.ebserh.paciente_api_ebserh.dto.response;

import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PacienteResponse {

    private UUID id;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private TipoSanguineo tipoSanguineo;

    private Boolean internado;

    private AmbienteInternacao ambienteInternacao;

    private Integer numeroLeito;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

}