package org.taxi.taxis.adapters.persistence;

import jakarta.persistence.*;
import org.taxi.taxis.domain.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "taxis")
public class TaxiFila {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, unique = true)
    public String placa;
    @Column(nullable = false)
    public String modelo;
    @Column(nullable = false)
    public String conductor;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoTaxi estado;
    public TaxiFila() {}
    public Taxi dominio() { return new Taxi(id, placa, modelo, conductor, estado); }
    public static TaxiFila de(Taxi valor) {
        var fila = new TaxiFila();
        fila.id = valor.id();
        fila.placa = valor.placa();
        fila.modelo = valor.modelo();
        fila.conductor = valor.conductor();
        fila.estado = valor.estado();
        return fila;
    }
}
