package org.citcuit.adapters.persistence;

import org.citcuit.application.ports.BuzonEventos;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class BuzonH2 implements BuzonEventos {
    private final EventosJpa jpa;
    public BuzonH2(EventosJpa jpa) { this.jpa=jpa; }
    public List<Pendiente> pendientes() { return jpa.findTop100ByOrderByCreadoAsc().stream().map(e->new Pendiente(e.id,e.contenido)).toList(); }
    public void confirmado(UUID id) { jpa.deleteById(id); }
    public long cantidad() { return jpa.count(); }
}
