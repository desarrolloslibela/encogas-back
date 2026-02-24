package ar.com.encogas.web.catalog;

import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import ar.com.encogas.dto.catalog.PuntoOperativoRequest;
import ar.com.encogas.dto.catalog.PuntoOperativoResponse;
import ar.com.encogas.service.catalog.PuntoOperativoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos-operativos")
public class PuntoOperativoController {

    private final PuntoOperativoService service;

    public PuntoOperativoController(PuntoOperativoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PuntoOperativoResponse> list(
            @RequestParam(required = false) PuntoOperativoService.Estado estado,
            @RequestParam(required = false) PuntoOperativoTipo tipo,
            @RequestParam(required = false) String search
    ) {
        return service.list(estado, tipo, search);
    }

    @PostMapping
    public PuntoOperativoResponse create(@RequestBody @Valid PuntoOperativoRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public PuntoOperativoResponse update(@PathVariable Long id, @RequestBody @Valid PuntoOperativoRequest req) {
        return service.update(id, req);
    }
}