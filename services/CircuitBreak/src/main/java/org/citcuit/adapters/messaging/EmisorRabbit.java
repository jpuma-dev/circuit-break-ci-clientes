package org.citcuit.adapters.messaging;

import org.citcuit.application.ports.EmisorEventos;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Component
public class EmisorRabbit implements EmisorEventos {
    private final RabbitTemplate rabbit;
    private static final org.slf4j.Logger log=org.slf4j.LoggerFactory.getLogger(EmisorRabbit.class);
    public EmisorRabbit(RabbitTemplate rabbit) { this.rabbit=rabbit; }
    public boolean enviar(UUID id,String contenido) {
        try {
            var confirmacion=new CorrelationData(id.toString());
            rabbit.convertAndSend("taxi.viajes","viaje.estado",contenido,confirmacion);
            return confirmacion.getFuture().get(3,TimeUnit.SECONDS).isAck() && confirmacion.getReturned()==null;
        } catch(InterruptedException e) { Thread.currentThread().interrupt();return false; }
        catch(Exception e) { log.warn("Evento {} pendiente de envio: {}",id,e.getClass().getSimpleName());return false; }
    }
}
