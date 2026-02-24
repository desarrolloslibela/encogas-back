package ar.com.encogas.dto.catalog;

import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PuntoOperativoRequest(
        @NotBlank @Size(min = 2, max = 20)
        String codigo,

        @NotBlank @Size(min = 2, max = 120)
        String nombre,

        @NotNull
        PuntoOperativoTipo tipo,

        @NotBlank @Size(min = 2, max = 200)
        String direccion,

        @Size(max = 120)
        String localidad,

        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
        BigDecimal latitud,

        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
        BigDecimal longitud
) {}