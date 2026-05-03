package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.ServicoPrestado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicoPrestadoRepository extends JpaRepository<ServicoPrestado, Integer> {

    @Query("""
           select s
           from ServicoPrestado s
           join s.cliente c
           where upper(c.nome) like upper(:nome)
           and (:mes is null or month(s.data) = :mes)
           """)
    Page<ServicoPrestado> buscarPorNomeClienteEMes(
            @Param("nome") String nome,
            @Param("mes") Integer mes,
            Pageable pageable
    );
}
