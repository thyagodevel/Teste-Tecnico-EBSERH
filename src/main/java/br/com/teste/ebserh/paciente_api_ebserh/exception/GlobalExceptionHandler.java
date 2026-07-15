package br.com.teste.ebserh.paciente_api_ebserh.exception;

import br.com.teste.ebserh.paciente_api_ebserh.dto.response.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarPacienteNaoEncontrado(
            PacienteNaoEncontradoException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(criarErro(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<ErroResponse> tratarCpfJaCadastrado(
            CpfJaCadastradoException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(criarErro(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(
            RegraNegocioException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.badRequest()
                .body(criarErro(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String mensagem = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(criarErro(
                        HttpStatus.BAD_REQUEST,
                        mensagem,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroGenerico(
            Exception ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarErro(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno do servidor.",
                        request.getRequestURI()
                ));
    }

    private ErroResponse criarErro(
            HttpStatus status,
            String mensagem,
            String caminho
    ) {

        return ErroResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(mensagem)
                .caminho(caminho)
                .build();
    }

}