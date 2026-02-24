package ar.com.encogas.web.fleet;

import ar.com.encogas.dto.fleet.VehiculoRequest;
import ar.com.encogas.dto.fleet.VehiculoResponse;
import ar.com.encogas.service.fleet.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) { this.service = service; }

    @GetMapping
    public List<VehiculoResponse> list(@RequestParam(required = false) VehiculoService.Estado estado) {
        return service.list(estado);
    }

    @PostMapping
    public VehiculoResponse create(@RequestBody @Valid VehiculoRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public VehiculoResponse update(@PathVariable Long id, @RequestBody @Valid VehiculoRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/activar")
    public void activar(@PathVariable Long id) { service.activar(id); }

    @PatchMapping("/{id}/desactivar")
    public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}