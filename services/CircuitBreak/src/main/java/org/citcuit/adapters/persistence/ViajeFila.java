package org.citcuit.adapters.persistence;

import jakarta.persistence.*;
import org.citcuit.domain.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "viajes")
public class ViajeFila {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public Long clienteId;
    public Long taxiId;
    @Column(nullable = false)
    public String origen;
    @Column(nullable = false)
    public String destino;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoViaje estado;
    @Column(nullable = false)
    public LocalDateTime fechaSolicitud;
    public ViajeFila() {}
    public Viaje dominio() { return new Viaje(id, clienteId, taxiId, origen, destino, estado, fechaSolicitud); }
    public static ViajeFila de(Viaje valor) {
        var fila = new ViajeFila();
        fila.id = valor.id();
        fila.clienteId = valor.clienteId();
        fila.taxiId = valor.taxiId();
        fila.origen = valor.origen();
        fila.destino = valor.destino();
        fila.estado = valor.estado();
        fila.fechaSolicitud = valor.fechaSolicitud();
        return fila;
    }
}
