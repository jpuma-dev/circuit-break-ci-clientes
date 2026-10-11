package org.taxi.taxis;
import org.taxi.taxis.domain.*;
import org.taxi.taxis.application.usecases.GestionTaxis;
import org.taxi.taxis.adapters.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties="eureka.client.enabled=false")
class TaxisIntegrationTest {
    @Autowired GestionTaxis casos;
    @Autowired TaxiJpa taxis;
    @Autowired ReservasJpa reservas;
    @BeforeEach void reset() { reservas.deleteAll();taxis.deleteAll(); }
    @Test void soloUnaReservaConcurrente() throws Exception {
        casos.crear("ABC-123","Sedan","Conductor",EstadoTaxi.DISPONIBLE);
        try(var pool=Executors.newFixedThreadPool(8)) {
            List<Callable<Boolean>> tareas=new ArrayList<>();
            for(long id=1;id<=20;id++) { long viaje=id;tareas.add(()->{try{casos.reservar(viaje);return true;}catch(ErrorNegocio e){assertEquals(ErrorNegocio.Tipo.CONFLICTO,e.tipo());return false;}}); }
            int exitos=0;for(var resultado:pool.invokeAll(tareas)) if(resultado.get()) exitos++;
            assertEquals(1,exitos);assertEquals(1,reservas.count());
        }
    }
    @Test void reservaYLiberaDeFormaIdempotente() {
        var taxi=casos.crear("ABC-123","Sedan","Conductor",EstadoTaxi.DISPONIBLE);
        assertEquals(casos.reservar(50),casos.reservar(50));
        assertThrows(ErrorNegocio.class,()->casos.cambiar(taxi.id(),EstadoTaxi.DISPONIBLE));
        casos.liberar(50);casos.liberar(50);
        assertEquals(EstadoTaxi.DISPONIBLE,casos.buscar(taxi.id()).estado());assertEquals(0,reservas.count());
    }
    @Test void mantenimientoNoSeAsignaYPlacaEsUnica() {
        casos.crear("ABC-123","Sedan","Conductor",EstadoTaxi.MANTENIMIENTO);
        assertThrows(ErrorNegocio.class,()->casos.reservar(1));
        assertThrows(ErrorNegocio.class,()->casos.crear("abc-123","Otro","Otro",EstadoTaxi.DISPONIBLE));
    }
}
