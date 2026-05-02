package io.github.dilluter.dto.login.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String accessToken;
    private String tokenType;
}