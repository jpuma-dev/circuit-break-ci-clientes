package org.citcuit.configuration;

import org.springframework.context.annotation.*;
import org.springframework.amqp.core.*;
@Configuration
public class RabbitConfig {
    @Bean DirectExchange viajesExchange() { return new DirectExchange("taxi.viajes",true,false); }
    @Bean Queue viajesQueue() { return QueueBuilder.durable("taxi.notificaciones").build(); }
    @Bean Binding viajesBinding(Queue viajesQueue,DirectExchange viajesExchange) {
        return BindingBuilder.bind(viajesQueue).to(viajesExchange).with("viaje.estado");
    }
}
