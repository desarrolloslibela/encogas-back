package ar.com.encogas.web.stock;

import ar.com.encogas.dto.stock.*;
import ar.com.encogas.service.stock.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockEnvaseRowResponse> stock(@RequestParam Long puntoOperativoId) {
        return service.stockPorPunto(puntoOperativoId);
    }

    @GetMapping("/movimientos")
    public List<MovimientoStockResponse> movimientos(@RequestParam Long puntoOperativoId) {
        return service.ultimosMovimientos(puntoOperativoId);
    }

    @PostMapping("/ajustes")
    public void ajustar(@RequestBody @Valid AjusteStockRequest req) {
        service.ajustar(req);
    }
}