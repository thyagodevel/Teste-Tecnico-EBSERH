package br.com.teste.ebserh.paciente_api_ebserh.service;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.AtualizarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.response.PacienteResponse;
import br.com.teste.ebserh.paciente_api_ebserh.entity.Paciente;
import br.com.teste.ebserh.paciente_api_ebserh.exception.CpfJaCadastradoException;
import br.com.teste.ebserh.paciente_api_ebserh.exception.PacienteNaoEncontradoException;
import br.com.teste.ebserh.paciente_api_ebserh.exception.RegraNegocioException;
import br.com.teste.ebserh.paciente_api_ebserh.mapper.PacienteMapper;
import br.com.teste.ebserh.paciente_api_ebserh.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    public PacienteResponse criarPaciente(CriarPacienteRequest request) {

        if (repository.existsByCpf(request.getCpf())) {
            throw new CpfJaCadastradoException();
        }

        validarInternacao(
                request.getInternado(),
                request.getAmbienteInternacao(),
                request.getNumeroLeito()
        );

        Paciente paciente = mapper.toEntity(request);

        paciente = repository.save(paciente);

        return mapper.toResponse(paciente);
    }

    private void validarInternacao(
            Boolean internado,
            br.com.teste.ebserh.paciente_api_ebserh.enums.AmbienteInternacao ambiente,
            Integer numeroLeito
    ) {

        if (Boolean.TRUE.equals(internado)) {

            if (ambiente == null) {
                throw new RegraNegocioException(
                        "O ambiente de internação é obrigatório para pacientes internados."
                );
            }

            if (numeroLeito == null) {
                throw new RegraNegocioException(
                        "O número do leito é obrigatório para pacientes internados."
                );
            }

            return;
        }

        if (ambiente != null) {
            throw new RegraNegocioException(
                    "Pacientes não internados não devem possuir ambiente de internação."
            );
        }

        if (numeroLeito != null) {
            throw new RegraNegocioException(
                    "Pacientes não internados não devem possuir número de leito."
            );
        }
    }

    public List<PacienteResponse> listarPacientes() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PacienteResponse buscarPorId(UUID id) {

        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        return mapper.toResponse(paciente);
    }

    public PacienteResponse atualizarPaciente(
            UUID id,
            AtualizarPacienteRequest request
    ) {

        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        if (repository.existsByCpfAndIdNot(request.getCpf(), id)) {
            throw new CpfJaCadastradoException();
        }

        validarInternacao(
                request.getInternado(),
                request.getAmbienteInternacao(),
                request.getNumeroLeito()
        );

        mapper.atualizarEntity(request, paciente);

        paciente = repository.save(paciente);

        return mapper.toResponse(paciente);
    }

    public void excluirPaciente(UUID id) {

        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        repository.delete(paciente);
    }

}