package ar.com.encogas.dto.sales;

import ar.com.encogas.domain.sales.VentaMetodoPago;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VentaResponse(
        Long id,
        Long jornadaId,
        Long clienteId,
        String clienteNombre,
        Instant fechaHora,
        VentaMetodoPago metodoPago,
        BigDecimal montoCobrado,
        BigDecimal totalVenta,
        BigDecimal saldoDeuda,
        String observacion,
        List<Item> items
) {
    public record Item(
            Long tipoEnvaseId,
            String tipoEnvaseNombre,
            String tipoEnvaseCodigo,
            String capacidadKg,
            String estadoEnvase,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {}
}