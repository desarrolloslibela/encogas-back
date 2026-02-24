package ar.com.encogas.security;

import ar.com.encogas.domain.security.Usuario;
import ar.com.encogas.exception.UnauthorizedException;
import ar.com.encogas.repository.security.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario requireUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedException("No autenticado");
        }
        String email = auth.getName();
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("No autenticado"));
    }
}