package ar.com.encogas.dto.auth;

import java.util.Set;

public record MeResponse(
        String email,
        Long empresaId,
        String empresaNombre,
        Set<String> roles
) {}