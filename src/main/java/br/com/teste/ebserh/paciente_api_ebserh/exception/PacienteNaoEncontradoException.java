package br.com.teste.ebserh.paciente_api_ebserh.exception;

public class PacienteNaoEncontradoException extends RuntimeException {

    public PacienteNaoEncontradoException() {
        super("Paciente não encontrado.");
    }

}