package org.taxi.notificaciones.application.usecases;

import org.taxi.notificaciones.application.ports.Notificaciones;
import org.taxi.notificaciones.domain.Notificacion;
import java.util.*;
public class RecibirNotificaciones {
    private final Notificaciones repositorio;
    public RecibirNotificaciones(Notificaciones repositorio) { this.repositorio=repositorio; }
    public synchronized void recibir(Notificacion evento) {
        if(!repositorio.existe(evento.eventoId())) repositorio.guardar(evento);
    }
    public List<Notificacion> consultar(Long viaje) {
        return repositorio.listar().stream().filter(n->viaje==null || viaje.equals(n.viajeId())).toList();
    }
}
