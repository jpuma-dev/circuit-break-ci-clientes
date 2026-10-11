package org.citcuit.application.usecases;

import org.citcuit.application.ports.*;
public class DespacharEventos {
    private final BuzonEventos buzon;
    private final EmisorEventos emisor;
    public DespacharEventos(BuzonEventos buzon,EmisorEventos emisor) { this.buzon=buzon;this.emisor=emisor; }
    public synchronized void ejecutar() {
        for(var evento:buzon.pendientes()) {
            if(!emisor.enviar(evento.id(),evento.contenido())) return;
            buzon.confirmado(evento.id());
        }
    }
}
