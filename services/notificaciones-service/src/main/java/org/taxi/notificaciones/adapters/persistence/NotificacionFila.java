package org.taxi.notificaciones.adapters.persistence;

import jakarta.persistence.*;
import org.taxi.notificaciones.domain.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "notificacions")
public class NotificacionFila {
    @Id
    public UUID eventoId;
    @Column(nullable = false)
    public Long viajeId;
    @Lob
    @Column(nullable = false)
    public String contenido;
    public NotificacionFila() {}
    public Notificacion dominio() { return new Notificacion(eventoId, viajeId, contenido); }
    public static NotificacionFila de(Notificacion valor) {
        var fila = new NotificacionFila();
        fila.eventoId = valor.eventoId();
        fila.viajeId = valor.viajeId();
        fila.contenido = valor.contenido();
        return fila;
    }
}
