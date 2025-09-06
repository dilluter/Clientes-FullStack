package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository <Cliente, Integer>{
    Optional<Cliente> findByCpf(String cpf);
}
