package ar.com.encogas.service.auth;

import ar.com.encogas.domain.security.Usuario;
import ar.com.encogas.dto.auth.*;
import ar.com.encogas.repository.security.UsuarioRepository;
import ar.com.encogas.security.JwtService;
import ar.com.encogas.exception.UnauthorizedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );

            Usuario u = usuarioRepository.findByEmailIgnoreCase(req.email())
                    .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

            var claims = new HashMap<String, Object>();
            claims.put("roles", u.getRoles().stream().map(Enum::name).toList());
            claims.put("empresaId", u.getEmpresa().getId());

            String token = jwtService.generateAccessToken(u.getEmail(), claims);
            return new AuthResponse(token, "Bearer");
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }

    public MeResponse me(String email) {
        Usuario u = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("No autenticado"));

        return new MeResponse(
                u.getEmail(),
                u.getEmpresa().getId(),
                u.getEmpresa().getNombre(),
                u.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }
}