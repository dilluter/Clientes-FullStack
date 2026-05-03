package io.github.dilluter.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCadastroDTO {

    @NotBlank(message = "Username obrigatório")
    private String username;

    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "Confirmação de senha obrigatória")
    private String confirmarPassword;

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Confirmação de email obrigatória")
    @Email(message = "Email de confirmação inválido")
    private String confirmarEmail;

    @NotBlank(message = "Nome completo obrigatório")
    private String nomeCompleto;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Telefone inválido")
    private String telefone;
}