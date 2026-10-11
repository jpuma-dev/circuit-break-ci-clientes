package org.taxi.notificaciones;
import org.taxi.notificaciones.adapters.persistence.NotificacionJpa;
import org.taxi.notificaciones.adapters.messaging.ReceptorRabbit;
import org.taxi.notificaciones.application.usecases.RecibirNotificaciones;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties={"eureka.client.enabled=false","spring.rabbitmq.listener.simple.auto-startup=false"})
class NotificacionesIntegrationTest {
    @Autowired ReceptorRabbit receptor;
    @Autowired RecibirNotificaciones casos;
    @Autowired NotificacionJpa jpa;
    @Test void deduplicaEnH2() {
        String evento="{\"eventoId\":\"11111111-1111-4111-8111-111111111111\",\"viaje\":{\"id\":1,\"estado\":\"COMPLETADO\"}}";
        receptor.recibir(evento);receptor.recibir(evento);
        assertEquals(1,jpa.count());assertEquals(1,casos.consultar(1L).size());assertEquals(0,casos.consultar(2L).size());
    }
}
