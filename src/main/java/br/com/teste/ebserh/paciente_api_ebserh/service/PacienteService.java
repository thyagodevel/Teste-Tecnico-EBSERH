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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    public PacienteResponse criarPaciente(CriarPacienteRequest request) {

        log.info("Iniciando cadastro do paciente. CPF={}", request.getCpf());

        if (repository.existsByCpf(request.getCpf())) {
            log.warn("Tentativa de cadastro com CPF já existente. CPF={}", request.getCpf());
            throw new CpfJaCadastradoException();
        }

        validarInternacao(
                request.getInternado(),
                request.getAmbienteInternacao(),
                request.getNumeroLeito()
        );

        Paciente paciente = mapper.toEntity(request);

        paciente = repository.save(paciente);

        log.info("Paciente cadastrado com sucesso. ID={}", paciente.getId());

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

    public Page<PacienteResponse> listarPacientes(Pageable pageable) {
        log.debug("Listando pacientes. Página={}, Tamanho={}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    public PacienteResponse buscarPorId(UUID id) {

        log.info("Buscando paciente. ID={}", id);

        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Paciente não encontrado. ID={}", id);
                    return new PacienteNaoEncontradoException();
                });

        return mapper.toResponse(paciente);
    }

    public PacienteResponse atualizarPaciente(
            UUID id,
            AtualizarPacienteRequest request
    ) {

        log.info("Atualizando paciente. ID={}", id);

        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        if (repository.existsByCpfAndIdNot(request.getCpf(), id)) {
            log.warn("Tentativa de atualização com CPF já cadastrado. ID={}, CPF={}",
                    id,
                    request.getCpf());
            throw new CpfJaCadastradoException();
        }

        validarInternacao(
                request.getInternado(),
                request.getAmbienteInternacao(),
                request.getNumeroLeito()
        );

        mapper.atualizarEntity(request, paciente);

        paciente = repository.save(paciente);

        log.info("Paciente atualizado com sucesso. ID={}", paciente.getId());

        return mapper.toResponse(paciente);
    }

    public void excluirPaciente(UUID id) {

        log.info("Excluindo paciente. ID={}", id);

        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Paciente não encontrado para exclusão. ID={}", id);
                    return new PacienteNaoEncontradoException();
                });

        repository.delete(paciente);

        log.info("Paciente excluído com sucesso. ID={}", id);
    }

}