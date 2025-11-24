package dev.trier.ecommerce.security;

import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenConfig tokenConfig;
    private final UsuarioService usuarioService;

    @Value("${app.oauth2.redirect-uri:http://localhost:3000/auth/callback}")
    private String frontendRedirectUri;

    public OAuth2LoginSuccessHandler(TokenConfig tokenConfig, UsuarioService usuarioService) {
        this.tokenConfig = tokenConfig;
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        try {
            UsuarioModel usuario = usuarioService.findByEmailOrCreateFromOAuth2(email, name, googleId);

            String token = tokenConfig.generateToken(usuario);

            String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                    .queryParam("token", token)
                    .queryParam("email", email)
                    .queryParam("cdUsuario", usuario.getCdUsuario())
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            String errorUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                    .queryParam("error", "authentication_failed")
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }
}