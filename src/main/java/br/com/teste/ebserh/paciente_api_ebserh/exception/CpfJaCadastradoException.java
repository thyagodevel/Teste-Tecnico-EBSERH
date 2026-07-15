package br.com.teste.ebserh.paciente_api_ebserh.exception;

public class CpfJaCadastradoException extends RuntimeException {

    public CpfJaCadastradoException() {
        super("Já existe um paciente cadastrado com este CPF.");
    }

}