package ar.com.encogas.dto.catalog;

import java.math.BigDecimal;

public record ClienteResponse(
        Long id,
        String razonSocial,
        String cuitDni,
        String direccion,
        String localidad,
        String telefono,
        String email,
        BigDecimal latitud,
        BigDecimal longitud,
        boolean activo
) {}