package ar.com.encogas.dto.catalog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ClienteRequest(
        @NotBlank @Size(min = 2, max = 150)
        String razonSocial,

        @Size(max = 20)
        String cuitDni,

        @NotBlank @Size(min = 2, max = 200)
        String direccion,

        @Size(max = 120)
        String localidad,

        @Size(max = 50)
        String telefono,

        @Email @Size(max = 190)
        String email,

        @DecimalMin(value = "-90.0", message = "Latitud fuera de rango")
        @DecimalMax(value = "90.0", message = "Latitud fuera de rango")
        BigDecimal latitud,

        @DecimalMin(value = "-180.0", message = "Longitud fuera de rango")
        @DecimalMax(value = "180.0", message = "Longitud fuera de rango")
        BigDecimal longitud
) {}