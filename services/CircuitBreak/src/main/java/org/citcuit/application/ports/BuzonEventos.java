package org.citcuit.application.ports;

import java.util.*;
public interface BuzonEventos {
    record Pendiente(UUID id,String contenido) {}
    List<Pendiente> pendientes();
    void confirmado(UUID id);
    long cantidad();
}
