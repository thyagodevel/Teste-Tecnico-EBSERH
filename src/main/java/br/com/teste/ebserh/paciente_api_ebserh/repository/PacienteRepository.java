package br.com.teste.ebserh.paciente_api_ebserh.repository;

import br.com.teste.ebserh.paciente_api_ebserh.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    Optional<Paciente> findByCpf(String cpf);

}