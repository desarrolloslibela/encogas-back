package ar.com.encogas.dto.fleet;

import jakarta.validation.constraints.*;

public record VehiculoRequest(
        @NotBlank @Size(min = 5, max = 15)
        String patente
) {}