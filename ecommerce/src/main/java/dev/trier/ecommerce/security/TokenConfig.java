package dev.trier.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import dev.trier.ecommerce.model.UsuarioModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    private final String secret = "secret";

    public String generateToken(UsuarioModel userModel){
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("cdUsuario", userModel.getCdUsuario())
                .withClaim("role", userModel.getUserRole() != null ? userModel.getUserRole().name() : null)
                .withSubject(userModel.getDsEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validadeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .cdUsuario(decode.getClaim("cdUsuario").asInt())
                    .dsEmail(decode.getSubject())
                    .role(decode.getClaim("role").asString())
                    .build());
        } catch (JWTVerificationException ex){
            return Optional.empty();
        }
    }
}