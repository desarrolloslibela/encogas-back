package ar.com.encogas.dto.stock;

import jakarta.validation.constraints.*;

public record AjusteStockRequest(
        @NotNull Long puntoOperativoId,
        @NotNull Long tipoEnvaseId,

        @Min(0) int nuevoLlenos,
        @Min(0) int nuevoVacios,

        @NotBlank @Size(min = 3, max = 255)
        String motivo
) {}