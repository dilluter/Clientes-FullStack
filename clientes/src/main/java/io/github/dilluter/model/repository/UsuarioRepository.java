package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Integer>{
}
