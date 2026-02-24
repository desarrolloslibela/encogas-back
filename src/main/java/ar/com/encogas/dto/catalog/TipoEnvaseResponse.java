package ar.com.encogas.dto.catalog;

import java.math.BigDecimal;

public record TipoEnvaseResponse(
        Long id,
        String codigo,
        String nombre,
        BigDecimal capacidadKg,
        boolean activo
) {}