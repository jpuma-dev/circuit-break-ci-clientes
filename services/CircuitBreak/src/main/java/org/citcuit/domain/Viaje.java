package org.citcuit.domain;

import java.time.LocalDateTime;
public record Viaje(Long id, Long clienteId, Long taxiId, String origen, String destino,
                    EstadoViaje estado, LocalDateTime fechaSolicitud) {
    public Viaje cambiar(Long taxi, EstadoViaje nuevo) {
        return new Viaje(id, clienteId, taxi, origen, destino, nuevo, fechaSolicitud);
    }
    public boolean coincide(Long cliente, String desde, String hasta) {
        return clienteId.equals(cliente) && origen.equals(desde) && destino.equals(hasta);
    }
}
