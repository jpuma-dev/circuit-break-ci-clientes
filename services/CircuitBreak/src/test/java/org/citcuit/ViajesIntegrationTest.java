package org.citcuit;
import org.citcuit.adapters.clients.*;
import org.citcuit.adapters.persistence.*;
import org.citcuit.application.usecases.GestionViajes;
import org.citcuit.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import io.github.resilience4j.circuitbreaker.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest(properties={"eureka.client.enabled=false","taxi.eventos.enabled=false"})
class ViajesIntegrationTest {
    @Autowired GestionViajes casos;
    @Autowired ViajeJpa viajes;
    @Autowired ClavesJpa claves;
    @Autowired EventosJpa eventos;
    @Autowired CircuitBreakerRegistry circuitos;
    @MockitoBean ClientesFeign clientes;
    @MockitoBean TaxisFeign taxis;
    @BeforeEach void reset() {
        claves.deleteAll();eventos.deleteAll();viajes.deleteAll();
        circuitos.circuitBreaker("clientes").reset();circuitos.circuitBreaker("taxis").reset();
        when(clientes.buscar(anyLong())).thenReturn(Map.of("id",1));
        when(taxis.reservar(anyLong())).thenReturn(new TaxisFeign.Respuesta(10L,"ABC","Sedan","Conductor","EN_SERVICIO"));
    }
    @Test void guardaViajeClaveYEventosEnH2() {
        var clave=UUID.randomUUID();var v=casos.solicitar(clave,1L,"A","B");
        assertEquals(EstadoViaje.ASIGNADO,v.estado());assertEquals(2,eventos.count());
        assertEquals(v,casos.solicitar(clave,1L,"A","B"));assertEquals(1,viajes.count());
        verify(taxis,times(1)).reservar(v.id());
        assertThrows(ErrorNegocio.class,()->casos.solicitar(clave,1L,"A","C"));
    }
    @Test void clienteInexistenteNoCreaViajeNiAbreCircuito() {
        when(clientes.buscar(anyLong())).thenThrow(mock(feign.FeignException.NotFound.class));
        for(int i=0;i<6;i++) assertThrows(ErrorNegocio.class,()->casos.solicitar(UUID.randomUUID(),1L,"A","B"));
        assertEquals(0,viajes.count());assertEquals(CircuitBreaker.State.CLOSED,circuitos.circuitBreaker("clientes").getState());
    }
    @Test void circuitoRealAbreYRecuperaPendientes() {
        when(clientes.buscar(anyLong())).thenThrow(new IllegalStateException("caido"));
        List<Viaje> pendientes=new ArrayList<>();
        for(int i=0;i<5;i++) pendientes.add(casos.solicitar(UUID.randomUUID(),1L,"A","B"));
        assertTrue(pendientes.stream().allMatch(v->v.estado()==EstadoViaje.PENDIENTE));
        var circuito=circuitos.circuitBreaker("clientes");assertEquals(CircuitBreaker.State.OPEN,circuito.getState());
        verify(clientes,times(4)).buscar(anyLong());
        doReturn(Map.of("id",1)).when(clientes).buscar(anyLong());circuito.transitionToHalfOpenState();
        assertEquals(EstadoViaje.ASIGNADO,casos.reintentar(pendientes.get(0).id()).estado());
        assertEquals(EstadoViaje.ASIGNADO,casos.reintentar(pendientes.get(1).id()).estado());
        assertEquals(CircuitBreaker.State.CLOSED,circuito.getState());
    }
    @Test void sinTaxiPuedeReintentarse() {
        when(taxis.reservar(anyLong())).thenThrow(mock(feign.FeignException.Conflict.class));
        var v=casos.solicitar(UUID.randomUUID(),1L,"A","B");assertEquals(EstadoViaje.SIN_TAXI_DISPONIBLE,v.estado());
        doReturn(new TaxisFeign.Respuesta(10L,"ABC","Sedan","Conductor","EN_SERVICIO")).when(taxis).reservar(anyLong());
        assertEquals(EstadoViaje.ASIGNADO,casos.reintentar(v.id()).estado());
    }
    @Test void noCompletaSiNoSeConfirmaLiberacion() {
        var v=casos.solicitar(UUID.randomUUID(),1L,"A","B");doThrow(new IllegalStateException()).when(taxis).liberar(v.id());
        assertThrows(ErrorNegocio.class,()->casos.completar(v.id()));assertEquals(EstadoViaje.ASIGNADO,casos.buscar(v.id()).estado());
        doNothing().when(taxis).liberar(v.id());assertEquals(EstadoViaje.COMPLETADO,casos.completar(v.id()).estado());
        casos.completar(v.id());verify(taxis,times(2)).liberar(v.id());
    }
    @Test void cancelacionDePendienteLiberaPosibleReserva() {
        when(taxis.reservar(anyLong())).thenThrow(new IllegalStateException("respuesta perdida"));
        var v=casos.solicitar(UUID.randomUUID(),1L,"A","B");
        assertEquals(EstadoViaje.CANCELADO,casos.cancelar(v.id()).estado());verify(taxis).liberar(v.id());
    }
}
