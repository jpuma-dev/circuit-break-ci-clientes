package org.taxi.taxis.domain;

public record Taxi(Long id, String placa, String modelo, String conductor, EstadoTaxi estado) {
    public Taxi conEstado(EstadoTaxi nuevo) { return new Taxi(id, placa, modelo, conductor, nuevo); }
}
