package org.citcuit.adapters.clients;

import org.citcuit.application.ports.TaxisRemotos;
import org.citcuit.domain.SinTaxi;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@Component
public class TaxisAdapter implements TaxisRemotos {
    private final TaxisFeign taxi;
    public TaxisAdapter(TaxisFeign taxi) { this.taxi=taxi; }
    @CircuitBreaker(name="taxis")
    public long reservar(long id) {
        try { return taxi.reservar(id).id(); }
        catch(feign.FeignException.Conflict e) { throw new SinTaxi(); }
    }
    @CircuitBreaker(name="taxis") public void liberar(long id) { taxi.liberar(id); }
}
