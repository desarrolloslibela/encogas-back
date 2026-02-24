package ar.com.encogas.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType
) {}