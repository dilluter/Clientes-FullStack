package io.github.dilluter.Service;

import io.github.dilluter.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder encoder;

    public TokenService(@Value("${jwt.secret}") String secret) {
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
    }

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("clientes-app")
                .subject(usuario.getUsername())
                .issuedAt(agora)
                .expiresAt(agora.plus(1, ChronoUnit.HOURS))
                .claim("email", usuario.getEmail())
                .build();

        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}