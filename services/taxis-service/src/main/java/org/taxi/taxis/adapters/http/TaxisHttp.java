package org.taxi.taxis.adapters.http;

import org.taxi.taxis.domain.*;
import org.taxi.taxis.application.usecases.GestionTaxis;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
@RestController
@RequestMapping("/api/taxis")
public class TaxisHttp {
    public record Entrada(@NotBlank String placa,@NotBlank String modelo,@NotBlank String conductor,@NotNull EstadoTaxi estado) {}
    public record Cambio(@NotNull EstadoTaxi estado) {}
    private final GestionTaxis casos;
    public TaxisHttp(GestionTaxis casos) { this.casos=casos; }
    @GetMapping public List<Taxi> listar(@RequestParam(required=false) EstadoTaxi estado) { return casos.listar(estado); }
    @GetMapping("/{id}") public Taxi buscar(@PathVariable long id) { return casos.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Taxi crear(@Valid @RequestBody Entrada e) { return casos.crear(e.placa(),e.modelo(),e.conductor(),e.estado()); }
    @PatchMapping("/{id}/estado") public Taxi cambiar(@PathVariable long id,@Valid @RequestBody Cambio c) { return casos.cambiar(id,c.estado()); }
    @PostMapping("/reservas/{viajeId}") public Taxi reservar(@PathVariable @Positive long viajeId) { return casos.reservar(viajeId); }
    @DeleteMapping("/reservas/{viajeId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void liberar(@PathVariable @Positive long viajeId) { casos.liberar(viajeId); }
}
