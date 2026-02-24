package ar.com.encogas.web.health;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public String health() { return "OK"; }
}