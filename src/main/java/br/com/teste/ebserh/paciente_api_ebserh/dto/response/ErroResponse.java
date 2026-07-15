package br.com.teste.ebserh.paciente_api_ebserh.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErroResponse {

    private LocalDateTime timestamp;

    private Integer status;

    private String erro;

    private String mensagem;

    private String caminho;

}