package org.taxi.taxis.adapters.persistence;

import jakarta.persistence.*;
@Entity
@Table(name="reservas", uniqueConstraints=@UniqueConstraint(columnNames="taxiId"))
public class ReservaFila {
    @Id public Long viajeId;
    @Column(nullable=false) public Long taxiId;
    public ReservaFila() {}
    ReservaFila(long viajeId, long taxiId) { this.viajeId=viajeId; this.taxiId=taxiId; }
}
