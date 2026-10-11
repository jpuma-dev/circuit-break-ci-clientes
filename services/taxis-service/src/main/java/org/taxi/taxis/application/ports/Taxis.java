package org.taxi.taxis.application.ports;

import org.taxi.taxis.domain.*;
import java.util.*;
public interface Taxis {
    List<Taxi> listar();
    Optional<Taxi> buscar(long id);
    Taxi guardar(Taxi taxi);
    boolean placaOcupada(String placa);
    Taxi reservar(long viajeId);
    void liberar(long viajeId);
    boolean reservado(long taxiId);
}
