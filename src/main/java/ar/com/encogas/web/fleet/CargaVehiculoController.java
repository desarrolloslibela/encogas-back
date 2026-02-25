package ar.com.encogas.web.fleet;

import ar.com.encogas.dto.fleet.CargaVehiculoRequest;
import ar.com.encogas.dto.fleet.CargaVehiculoResponse;
import ar.com.encogas.service.fleet.CargaVehiculoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargas-vehiculo")
public class CargaVehiculoController {

    private final CargaVehiculoService service;

    public CargaVehiculoController(CargaVehiculoService service) { this.service = service; }

    @GetMapping
    public List<CargaVehiculoResponse> list(@RequestParam Long jornadaId) {
        return service.listar(jornadaId);
    }

    @PostMapping
    public CargaVehiculoResponse crear(@RequestBody @Valid CargaVehiculoRequest req) {
        return service.crear(req);
    }
}