package org.taxi.taxis.application.usecases;

import org.taxi.taxis.domain.*;
import org.taxi.taxis.application.ports.Taxis;
import static org.taxi.taxis.domain.ErrorNegocio.Tipo.*;
import java.util.*;
public class GestionTaxis {
    private final Taxis taxis;
    public GestionTaxis(Taxis taxis) { this.taxis=taxis; }
    public synchronized List<Taxi> listar(EstadoTaxi estado) { return taxis.listar().stream().filter(t->estado==null || t.estado()==estado).toList(); }
    public synchronized Taxi buscar(long id) { return taxis.buscar(id).orElseThrow(()->new ErrorNegocio(NO_EXISTE,"Taxi inexistente")); }
    public synchronized Taxi crear(String placa,String modelo,String conductor,EstadoTaxi estado) {
        String normalizada=placa.trim().toUpperCase(Locale.ROOT);
        if(taxis.placaOcupada(normalizada)) throw new ErrorNegocio(CONFLICTO,"Placa ya registrada");
        return taxis.guardar(new Taxi(null,normalizada,modelo,conductor,estado));
    }
    public synchronized Taxi cambiar(long id,EstadoTaxi estado) {
        Taxi taxi=buscar(id);
        if(taxis.reservado(id)) throw new ErrorNegocio(CONFLICTO,"Finalice o cancele el viaje antes de cambiar su taxi");
        return taxis.guardar(taxi.conEstado(estado));
    }
    public synchronized Taxi reservar(long viajeId) { return taxis.reservar(viajeId); }
    public synchronized void liberar(long viajeId) { taxis.liberar(viajeId); }
}
