package org.citcuit.adapters.persistence;

import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="claves_solicitud")
public class ClaveFila {
    @Id public UUID clave;
    @Column(nullable=false,unique=true) public Long viajeId;
    public ClaveFila() {}
    ClaveFila(UUID clave,Long viajeId) { this.clave=clave;this.viajeId=viajeId; }
}
