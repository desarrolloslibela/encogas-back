package ar.com.encogas.web.security;

import ar.com.encogas.dto.security.DriverResponse;
import ar.com.encogas.service.security.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) { this.service = service; }

    @GetMapping
    public List<DriverResponse> list() { return service.listDrivers(); }
}