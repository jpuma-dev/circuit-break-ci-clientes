package org.citcuit.adapters.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name="clientes-service")
public interface ClientesFeign {
    @GetMapping("/api/clientes/{id}") Object buscar(@PathVariable("id") long id);
}
