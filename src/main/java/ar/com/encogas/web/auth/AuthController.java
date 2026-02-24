package ar.com.encogas.web.auth;

import ar.com.encogas.dto.auth.*;
import ar.com.encogas.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest req) {
        return authService.login(req);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal UserDetails user) {
        return authService.me(user.getUsername());
    }
}