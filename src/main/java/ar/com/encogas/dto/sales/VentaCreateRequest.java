package ar.com.encogas.dto.sales;

import ar.com.encogas.domain.sales.EstadoEnvaseVenta;
import ar.com.encogas.domain.sales.VentaMetodoPago;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record VentaCreateRequest(
        @NotNull Long jornadaId,
        @NotNull Long clienteId,
        @NotNull VentaMetodoPago metodoPago,
        @NotNull @DecimalMin("0.00") BigDecimal montoCobrado,
        @Size(max = 255) String observacion,
        @Size(min = 1) List<Item> items
) {
    public record Item(
            @NotNull Long tipoEnvaseId,
            @NotNull EstadoEnvaseVenta estadoEnvase,
            @Min(1) int cantidad,
            @NotNull @DecimalMin("0.00") BigDecimal precioUnitario
    ) {}
}