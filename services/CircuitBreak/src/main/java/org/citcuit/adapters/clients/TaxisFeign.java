package org.citcuit.adapters.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name="taxis-service")
public interface TaxisFeign {
    record Respuesta(Long id,String placa,String modelo,String conductor,String estado) {}
    @PostMapping("/api/taxis/reservas/{id}") Respuesta reservar(@PathVariable("id") long viajeId);
    @DeleteMapping("/api/taxis/reservas/{id}") void liberar(@PathVariable("id") long viajeId);
}
