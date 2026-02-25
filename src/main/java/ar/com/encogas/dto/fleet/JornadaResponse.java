package ar.com.encogas.dto.fleet;

import ar.com.encogas.domain.fleet.JornadaEstado;

import java.time.LocalDate;

public record JornadaResponse(
        Long id,
        LocalDate fecha,
        JornadaEstado estado,
        Long vehiculoId,
        String vehiculoPatente,
        Long choferUsuarioId,
        String choferEmail,
        Long puntoOperativoOrigenId,
        String puntoOperativoOrigenNombre,
        Long puntoOperativoVehiculoId
) {}