package ar.com.encogas.web.catalog;

import ar.com.encogas.dto.catalog.TipoEnvaseRequest;
import ar.com.encogas.dto.catalog.TipoEnvaseResponse;
import ar.com.encogas.service.catalog.TipoEnvaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-envase")
public class TipoEnvaseController {

    private final TipoEnvaseService service;

    public TipoEnvaseController(TipoEnvaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<TipoEnvaseResponse> list(
            @RequestParam(required = false) TipoEnvaseService.Estado estado,
            @RequestParam(required = false) String search
    ) {
        return service.list(estado, search);
    }

    @GetMapping("/{id}")
    public TipoEnvaseResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public TipoEnvaseResponse create(@RequestBody @Valid TipoEnvaseRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public TipoEnvaseResponse update(@PathVariable Long id, @RequestBody @Valid TipoEnvaseRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/activar")
    public void activar(@PathVariable Long id) {
        service.activar(id);
    }

    @PatchMapping("/{id}/desactivar")
    public void desactivar(@PathVariable Long id) {
        service.desactivar(id);
    }
}