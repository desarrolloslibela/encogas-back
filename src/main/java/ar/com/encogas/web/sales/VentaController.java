package ar.com.encogas.web.sales;

import ar.com.encogas.dto.sales.VentaCreateRequest;
import ar.com.encogas.dto.sales.VentaResponse;
import ar.com.encogas.service.sales.VentaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<VentaResponse> list(@RequestParam Long jornadaId) {
        return service.ultimasPorJornada(jornadaId);
    }

    @GetMapping("/{id}")
    public VentaResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public VentaResponse crear(@RequestBody @Valid VentaCreateRequest req) {
        return service.crear(req);
    }
}