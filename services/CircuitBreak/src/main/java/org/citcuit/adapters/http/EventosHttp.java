package org.citcuit.adapters.http;

import org.citcuit.application.ports.BuzonEventos;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
public class EventosHttp {
    private final BuzonEventos buzon;
    public EventosHttp(BuzonEventos buzon) { this.buzon=buzon; }
    @GetMapping("/api/viajes/eventos/pendientes") public Map<String,Long> pendientes() { return Map.of("pendientes",buzon.cantidad()); }
}
