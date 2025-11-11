package dev.trier.ecommerce.security;

import dev.trier.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthConfig implements UserDetailsService {
    private final UsuarioRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByDsEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
