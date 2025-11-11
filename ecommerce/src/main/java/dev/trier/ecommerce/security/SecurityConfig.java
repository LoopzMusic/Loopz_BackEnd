package dev.trier.ecommerce.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter){
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http
                .csrf(csrf -> csrf.disable())
                .cors( cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "auth/register").permitAll()


                        .requestMatchers(HttpMethod.GET, "produto/{cdProduto}/imagem").permitAll()
                        .requestMatchers(HttpMethod.GET, "produto/listar/todos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/produto/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/produto/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/produto/{cdProduto}/idProduto").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/produto/{nmProduto}").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET,"/usuario/buscar/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuario/listar/usuarios").hasRole("ADMIN")


                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()


                        .requestMatchers(HttpMethod.POST, "/empresa/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/empresa/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/empresa/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/empresa/listar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/empresa/listarCNPJ/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.POST, "/estoque/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/estoque/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/estoque/listar/**").hasRole("ADMIN")


                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
