package ar.com.encogas.dto.catalog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TipoEnvaseRequest(
        @NotBlank @Size(min = 2, max = 20) @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Código inválido (solo letras/números/_/-)")
        String codigo,

        @NotBlank @Size(min = 2, max = 50)
        String nombre,

        @NotNull @DecimalMin(value = "0.01", message = "La capacidad debe ser mayor a 0")
        BigDecimal capacidadKg
) {}