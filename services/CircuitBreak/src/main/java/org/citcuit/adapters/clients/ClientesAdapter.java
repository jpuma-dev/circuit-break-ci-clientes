package org.citcuit.adapters.clients;

import org.citcuit.application.ports.ClientesRemotos;
import org.citcuit.domain.ClienteInexistente;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@Component
public class ClientesAdapter implements ClientesRemotos {
    private final ClientesFeign cliente;
    public ClientesAdapter(ClientesFeign cliente) { this.cliente=cliente; }
    @CircuitBreaker(name="clientes")
    public void comprobar(long id) {
        try { cliente.buscar(id); }
        catch(feign.FeignException.NotFound e) { throw new ClienteInexistente(); }
    }
}
