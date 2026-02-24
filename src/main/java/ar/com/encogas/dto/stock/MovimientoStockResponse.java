package ar.com.encogas.dto.stock;

import ar.com.encogas.domain.stock.MovimientoStockTipo;

import java.time.Instant;

public record MovimientoStockResponse(
        Long id,
        MovimientoStockTipo tipo,
        String motivo,
        int deltaLlenos,
        int deltaVacios,
        int saldoLlenos,
        int saldoVacios,
        String referencia,
        Instant createdAt
) {}