package org.citcuit.adapters.messaging;

import org.citcuit.application.usecases.DespacharEventos;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
@Configuration @EnableScheduling
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name="taxi.eventos.enabled",havingValue="true",matchIfMissing=true)
public class TareaEventos {
    private final DespacharEventos casos;
    public TareaEventos(DespacharEventos casos) { this.casos=casos; }
    @Scheduled(fixedDelayString="${taxi.eventos.delay:3000}") public void publicar() { casos.ejecutar(); }
}
