package org.taxi.notificaciones.adapters.messaging;

import org.taxi.notificaciones.domain.Notificacion;
import org.taxi.notificaciones.application.usecases.RecibirNotificaciones;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
@Component
public class ReceptorRabbit {
    private final RecibirNotificaciones casos;
    private final ObjectMapper json;
    public ReceptorRabbit(RecibirNotificaciones casos,ObjectMapper json) { this.casos=casos;this.json=json; }
    @RabbitListener(queues="taxi.notificaciones")
    public void recibir(String mensaje) {
        final Notificacion evento;
        try {
            var raiz=json.readTree(mensaje);
            evento=new Notificacion(UUID.fromString(raiz.required("eventoId").asText()),raiz.required("viaje").required("id").longValue(),mensaje);
        } catch(Exception e) { throw new AmqpRejectAndDontRequeueException("Evento invalido",e); }
        casos.recibir(evento);
    }
}
