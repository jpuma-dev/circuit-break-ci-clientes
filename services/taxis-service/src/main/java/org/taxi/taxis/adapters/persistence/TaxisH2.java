package org.taxi.taxis.adapters.persistence;

import org.taxi.taxis.application.ports.Taxis;
import org.taxi.taxis.domain.*;
import static org.taxi.taxis.domain.ErrorNegocio.Tipo.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Repository
public class TaxisH2 implements Taxis {
    private final TaxiJpa taxis;
    private final ReservasJpa reservas;
    public TaxisH2(TaxiJpa taxis, ReservasJpa reservas) { this.taxis=taxis; this.reservas=reservas; }
    public List<Taxi> listar() { return taxis.findAll().stream().map(TaxiFila::dominio).toList(); }
    public Optional<Taxi> buscar(long id) { return taxis.findById(id).map(TaxiFila::dominio); }
    public Taxi guardar(Taxi taxi) { return taxis.saveAndFlush(TaxiFila.de(taxi)).dominio(); }
    public boolean placaOcupada(String placa) { return taxis.existsByPlacaIgnoreCase(placa); }
    public boolean reservado(long taxiId) { return reservas.existsByTaxiId(taxiId); }
    @Transactional
    public Taxi reservar(long viajeId) {
        var anterior=reservas.findById(viajeId);
        if(anterior.isPresent()) return taxis.findById(anterior.get().taxiId).orElseThrow().dominio();
        // Selection and occupation occur in the same transaction under a database lock.
        var libres=taxis.disponibles(EstadoTaxi.DISPONIBLE);
        if(libres.isEmpty()) throw new ErrorNegocio(CONFLICTO,"No hay taxis disponibles");
        var elegido=libres.getFirst(); elegido.estado=EstadoTaxi.EN_SERVICIO;
        taxis.save(elegido);
        reservas.saveAndFlush(new ReservaFila(viajeId,elegido.id));
        return elegido.dominio();
    }
    @Transactional
    public void liberar(long viajeId) {
        reservas.findById(viajeId).ifPresent(reserva -> {
            var taxi=taxis.findById(reserva.taxiId).orElseThrow();
            taxi.estado=EstadoTaxi.DISPONIBLE;
            taxis.save(taxi); reservas.delete(reserva);
        });
    }
}
