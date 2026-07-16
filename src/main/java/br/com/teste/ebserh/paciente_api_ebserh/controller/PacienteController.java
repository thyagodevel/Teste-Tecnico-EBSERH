package br.com.teste.ebserh.paciente_api_ebserh.controller;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.AtualizarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.response.PacienteResponse;
import br.com.teste.ebserh.paciente_api_ebserh.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @Operation(summary = "Criar paciente")
    @PostMapping
    public ResponseEntity<PacienteResponse> criar(
            @RequestBody @Valid CriarPacienteRequest request
    ) {

        PacienteResponse response = service.criarPaciente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Listar pacientes com paginação")
    @GetMapping
    public ResponseEntity<Page<PacienteResponse>> listarPacientes(
            @PageableDefault(size = 10, sort = "nome")
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarPacientes(pageable));
    }

    @Operation(summary = "Buscar paciente por Id")
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Atualizar dados do paciente")
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarPacienteRequest request
    ) {

        return ResponseEntity.ok(
                service.atualizarPaciente(id, request)
        );
    }

    @Operation(summary = "Excluir paciente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable UUID id
    ) {

        service.excluirPaciente(id);

        return ResponseEntity.noContent().build();
    }

}