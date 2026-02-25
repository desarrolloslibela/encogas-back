package ar.com.encogas.dto.fleet;

import jakarta.validation.constraints.*;

import java.util.List;

public record CargaVehiculoRequest(
        @NotNull Long jornadaId,
        @Size(min = 1, message = "Debe incluir al menos un ítem")
        List<Item> items,
        @Size(max = 255) String observacion
) {
    public record Item(
            @NotNull Long tipoEnvaseId,
            @Min(0) int cantLlenos,
            @Min(0) int cantVacios
    ) {}
}