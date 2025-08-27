package io.github.dilluter.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome",nullable = false, length = 150)
    private String nome;
    @Column(name = "cpf",nullable = false, length = 11)
    private String cpf;
    @Column(name = "data_cadastro")
    private LocalDate dataDeCadastro;
    @PrePersist
    public void prePersist(){
        setDataDeCadastro(LocalDate.now());
    }

}
