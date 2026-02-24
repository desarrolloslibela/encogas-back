package ar.com.encogas.dto.stock;

public record StockEnvaseRowResponse(
        Long tipoEnvaseId,
        String tipoEnvaseCodigo,
        String tipoEnvaseNombre,
        String tipoEnvaseCapacidadKg,
        int llenos,
        int vacios
) {}