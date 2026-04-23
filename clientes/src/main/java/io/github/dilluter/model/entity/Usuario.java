package io.github.dilluter.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, length = 150, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 150, unique = true)
    private String password;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nomeCompleto;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Telefone inválido")
    @Column
    private String telefone;
}
