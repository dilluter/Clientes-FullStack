package io.github.dilluter.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class cliente {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, length = 11)
    private String cpf;
    @Column
    private LocalDate dataDeCadastro;
}
