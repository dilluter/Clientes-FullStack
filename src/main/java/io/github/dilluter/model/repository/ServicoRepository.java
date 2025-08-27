package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
}
