package ar.com.encogas.dto.fleet;

import java.time.Instant;
import java.util.List;

public record CargaVehiculoResponse(
        Long id,
        Long jornadaId,
        Instant fechaHora,
        String observacion,
        Long origenId,
        Long vehiculoPoId,
        List<Item> items
) {
    public record Item(Long tipoEnvaseId, String tipoEnvaseNombre, String tipoEnvaseCodigo, String capacidadKg, int cantLlenos, int cantVacios) {}
}