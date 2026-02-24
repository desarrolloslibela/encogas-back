package ar.com.encogas.dto.fleet;

public record VehiculoResponse(
        Long id,
        String patente,
        Long puntoOperativoId,
        boolean activo
) {}