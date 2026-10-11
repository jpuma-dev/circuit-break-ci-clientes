package org.taxi.notificaciones.adapters.persistence;

import org.taxi.notificaciones.application.ports.Notificaciones;
import org.taxi.notificaciones.domain.Notificacion;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class NotificacionesH2 implements Notificaciones {
    private final NotificacionJpa jpa;
    public NotificacionesH2(NotificacionJpa jpa) { this.jpa=jpa; }
    public boolean existe(UUID id) { return jpa.existsById(id); }
    public void guardar(Notificacion n) { jpa.saveAndFlush(NotificacionFila.de(n)); }
    public List<Notificacion> listar() { return jpa.findAll().stream().map(NotificacionFila::dominio).toList(); }
}
