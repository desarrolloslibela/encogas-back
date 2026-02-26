package ar.com.encogas.web.fleet;

import ar.com.encogas.dto.fleet.JornadaCreateRequest;
import ar.com.encogas.dto.fleet.JornadaResponse;
import ar.com.encogas.service.fleet.JornadaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {

    private final JornadaService service;

    public JornadaController(JornadaService service) { this.service = service; }

    @GetMapping
    public List<JornadaResponse> ultimas() { return service.ultimas(); }

    @PostMapping
    public JornadaResponse crear(@RequestBody @Valid JornadaCreateRequest req) { return service.crear(req); }

    @PatchMapping("/{id}/cerrar")
    public void cerrar(@PathVariable Long id) { service.cerrar(id); }

    @GetMapping("/mi-abierta")
    @PreAuthorize("hasRole('DRIVER')")
    public JornadaResponse miAbierta() {
        return service.miAbierta();
    }
}