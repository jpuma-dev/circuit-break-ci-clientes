package org.citcuit.adapters.persistence;

import org.citcuit.application.ports.RegistroViajes;
import org.citcuit.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
@Repository
public class ViajesH2 implements RegistroViajes {
    private final ViajeJpa viajes;
    private final ClavesJpa claves;
    private final EventosJpa eventos;
    private final ObjectMapper json;
    public ViajesH2(ViajeJpa viajes,ClavesJpa claves,EventosJpa eventos,ObjectMapper json) {
        this.viajes=viajes;this.claves=claves;this.eventos=eventos;this.json=json;
    }
    public Optional<Viaje> porClave(UUID clave) { return claves.findById(clave).flatMap(c->buscar(c.viajeId)); }
    public Optional<Viaje> buscar(long id) { return viajes.findById(id).map(ViajeFila::dominio); }
    public List<Viaje> listar() { return viajes.findAll().stream().map(ViajeFila::dominio).toList(); }
    @Transactional
    public Viaje crear(UUID clave,Viaje viaje) {
        var guardado=viajes.saveAndFlush(ViajeFila.de(viaje)).dominio();
        claves.saveAndFlush(new ClaveFila(clave,guardado.id()));
        evento(guardado);
        return guardado;
    }
    @Transactional
    public Viaje guardar(Viaje viaje) {
        var anterior=viajes.findById(viaje.id()).orElseThrow().estado;
        var guardado=viajes.saveAndFlush(ViajeFila.de(viaje)).dominio();
        if(anterior!=guardado.estado()) evento(guardado);
        return guardado;
    }
    // State and pending event are committed together; a broker outage cannot lose this event.
    private void evento(Viaje viaje) {
        var fila=new EventoFila();fila.id=UUID.randomUUID();fila.creado=java.time.Instant.now();
        try { fila.contenido=json.writeValueAsString(new EventoViaje(fila.id,viaje)); }
        catch(com.fasterxml.jackson.core.JsonProcessingException e) { throw new IllegalStateException("No se pudo serializar el evento",e); }
        eventos.save(fila);
    }
}
