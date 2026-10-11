package org.citcuit.adapters.persistence;

import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="eventos_pendientes")
public class EventoFila {
    @Id public UUID id;
    @Lob @Column(nullable=false) public String contenido;
    @Column(nullable=false) public java.time.Instant creado;
    public EventoFila() {}
}
