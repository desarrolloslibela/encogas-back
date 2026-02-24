package ar.com.encogas.web.catalog;

import ar.com.encogas.dto.catalog.ClienteRequest;
import ar.com.encogas.dto.catalog.ClienteResponse;
import ar.com.encogas.service.catalog.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteResponse> list(
            @RequestParam(required = false) ClienteService.Estado estado,
            @RequestParam(required = false) String search
    ) {
        return service.list(estado, search);
    }

    @GetMapping("/{id}")
    public ClienteResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ClienteResponse create(@RequestBody @Valid ClienteRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ClienteResponse update(@PathVariable Long id, @RequestBody @Valid ClienteRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/activar")
    public void activar(@PathVariable Long id) { service.activar(id); }

    @PatchMapping("/{id}/desactivar")
    public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}