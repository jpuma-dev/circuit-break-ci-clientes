package org.citcuit.adapters.http;

import org.citcuit.domain.*;
import org.citcuit.application.usecases.GestionViajes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
@RestController @RequestMapping("/api/viajes")
public class ViajesHttp {
    public record Entrada(@NotNull @Positive Long clienteId,@NotBlank String origen,@NotBlank String destino) {}
    private final GestionViajes casos;
    public ViajesHttp(GestionViajes casos) { this.casos=casos; }
    @GetMapping public List<Viaje> listar() { return casos.listar(); }
    @GetMapping("/{id}") public Viaje buscar(@PathVariable long id) { return casos.buscar(id); }
    @PostMapping public ResponseEntity<Viaje> crear(@RequestHeader("Idempotency-Key") UUID clave,@Valid @RequestBody Entrada e) {
        return respuesta(casos.solicitar(clave,e.clienteId(),e.origen(),e.destino()));
    }
    @PostMapping("/{id}/reintentar") public ResponseEntity<Viaje> reintentar(@PathVariable long id) { return respuesta(casos.reintentar(id)); }
    @PostMapping("/{id}/completar") public Viaje completar(@PathVariable long id) { return casos.completar(id); }
    @PostMapping("/{id}/cancelar") public Viaje cancelar(@PathVariable long id) { return casos.cancelar(id); }
    private ResponseEntity<Viaje> respuesta(Viaje v) {
        return ResponseEntity.status(v.estado()==EstadoViaje.PENDIENTE || v.estado()==EstadoViaje.SIN_TAXI_DISPONIBLE ? 202 : 200).body(v);
    }
}
