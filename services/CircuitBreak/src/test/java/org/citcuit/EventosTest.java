package org.citcuit;
import org.citcuit.application.ports.*;
import org.citcuit.application.usecases.DespacharEventos;
import org.citcuit.adapters.messaging.EmisorRabbit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class EventosTest {
    @Test void pendienteSoloSeRetiraTrasConfirmacion() {
        var buzon=mock(BuzonEventos.class);var emisor=mock(EmisorEventos.class);var id=UUID.randomUUID();
        when(buzon.pendientes()).thenReturn(List.of(new BuzonEventos.Pendiente(id,"{}")));
        var casos=new DespacharEventos(buzon,emisor);casos.ejecutar();verify(buzon,never()).confirmado(any());
        when(emisor.enviar(id,"{}")).thenReturn(true);casos.ejecutar();verify(buzon).confirmado(id);
    }
    @Test void brokerCaidoNoDescartaEvento() {
        var rabbit=mock(RabbitTemplate.class);var adapter=new EmisorRabbit(rabbit);
        doThrow(new IllegalStateException()).when(rabbit).convertAndSend(anyString(),anyString(),any(Object.class),any(CorrelationData.class));
        assertFalse(adapter.enviar(UUID.randomUUID(),"{}"));
        doAnswer(i->{i.getArgument(3,CorrelationData.class).getFuture().complete(new CorrelationData.Confirm(true,null));return null;}).when(rabbit).convertAndSend(anyString(),anyString(),any(Object.class),any(CorrelationData.class));
        assertTrue(adapter.enviar(UUID.randomUUID(),"{}"));
    }
}
