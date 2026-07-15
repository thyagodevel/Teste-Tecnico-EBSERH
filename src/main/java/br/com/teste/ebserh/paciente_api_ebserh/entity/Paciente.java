package br.com.teste.ebserh.paciente_api_ebserh.entity;


import br.com.teste.ebserh.paciente_api_ebserh.enums.TipoSanguineo;
import br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(
        name = "pacientes",
        indexes = {
                @Index(name = "idx_paciente_cpf", columnList = "cpf", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Paciente extends EntidadeBase {

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sanguineo", nullable = false, length = 20)
    private TipoSanguineo tipoSanguineo;

    @Column(name = "internado", nullable = false)
    private Boolean internado;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente_internacao", length = 30)
    private AmbienteInternacao ambienteInternacao;

    @Column(name = "numero_leito")
    private Integer numeroLeito;
}