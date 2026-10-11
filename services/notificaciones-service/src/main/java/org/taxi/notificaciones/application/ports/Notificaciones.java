package org.taxi.notificaciones.application.ports;

import org.taxi.notificaciones.domain.Notificacion;
import java.util.*;
public interface Notificaciones { boolean existe(UUID id); void guardar(Notificacion n); List<Notificacion> listar(); }
