package br.com.teste.ebserh.paciente_api_ebserh.controller;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.AtualizarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.response.PacienteResponse;
import br.com.teste.ebserh.paciente_api_ebserh.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @PostMapping
    public ResponseEntity<PacienteResponse> criar(
            @RequestBody @Valid CriarPacienteRequest request
    ) {

        PacienteResponse response = service.criarPaciente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listar() {

        return ResponseEntity.ok(
                service.listarPacientes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarPacienteRequest request
    ) {

        return ResponseEntity.ok(
                service.atualizarPaciente(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable UUID id
    ) {

        service.excluirPaciente(id);

        return ResponseEntity.noContent().build();
    }

}