package br.com.teste.ebserh.paciente_api_ebserh.dto.request;

import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
public abstract class PacienteRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome deve possuir entre 3 e 150 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido.")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser anterior à data atual.")
    private LocalDate dataNascimento;

    @NotNull(message = "O tipo sanguíneo é obrigatório.")
    private TipoSanguineo tipoSanguineo;

    @NotNull(message = "O campo internado é obrigatório.")
    private Boolean internado;

    private AmbienteInternacao ambienteInternacao;

    @Positive(message = "O número do leito deve ser maior que zero.")
    private Integer numeroLeito;

}