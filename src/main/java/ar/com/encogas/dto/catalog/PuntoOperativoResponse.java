package ar.com.encogas.dto.catalog;

import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import java.math.BigDecimal;

public record PuntoOperativoResponse(
        Long id,
        String codigo,
        String nombre,
        PuntoOperativoTipo tipo,
        String direccion,
        String localidad,
        BigDecimal latitud,
        BigDecimal longitud,
        boolean activo
) {}