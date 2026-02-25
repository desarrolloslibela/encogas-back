package ar.com.encogas.dto.fleet;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record JornadaCreateRequest(
        @NotNull LocalDate fecha,
        @NotNull Long vehiculoId,
        @NotNull Long choferUsuarioId,
        @NotNull Long puntoOperativoOrigenId
) {}