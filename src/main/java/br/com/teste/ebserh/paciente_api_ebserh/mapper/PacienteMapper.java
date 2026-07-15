package br.com.teste.ebserh.paciente_api_ebserh.mapper;

import br.com.teste.ebserh.paciente_api_ebserh.dto.request.AtualizarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.request.CriarPacienteRequest;
import br.com.teste.ebserh.paciente_api_ebserh.dto.response.PacienteResponse;
import br.com.teste.ebserh.paciente_api_ebserh.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(CriarPacienteRequest request) {
        return Paciente.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .dataNascimento(request.getDataNascimento())
                .tipoSanguineo(request.getTipoSanguineo())
                .internado(request.getInternado())
                .ambienteInternacao(request.getAmbienteInternacao())
                .numeroLeito(request.getNumeroLeito())
                .build();
    }

    public void atualizarEntity(AtualizarPacienteRequest request, Paciente paciente) {
        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setTipoSanguineo(request.getTipoSanguineo());
        paciente.setInternado(request.getInternado());
        paciente.setAmbienteInternacao(request.getAmbienteInternacao());
        paciente.setNumeroLeito(request.getNumeroLeito());
    }

    public PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .cpf(paciente.getCpf())
                .dataNascimento(paciente.getDataNascimento())
                .tipoSanguineo(paciente.getTipoSanguineo())
                .internado(paciente.getInternado())
                .ambienteInternacao(paciente.getAmbienteInternacao())
                .numeroLeito(paciente.getNumeroLeito())
                .criadoEm(paciente.getCriadoEm())
                .atualizadoEm(paciente.getAtualizadoEm())
                .build();
    }

}