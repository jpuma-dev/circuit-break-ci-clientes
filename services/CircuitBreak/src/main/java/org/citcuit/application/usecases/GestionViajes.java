package org.citcuit.application.usecases;

import org.citcuit.domain.*;
import org.citcuit.application.ports.*;
import static org.citcuit.domain.ErrorNegocio.Tipo.*;
import static org.citcuit.domain.EstadoViaje.*;
import java.time.LocalDateTime;
import java.util.*;
public class GestionViajes {
    private final RegistroViajes registro;
    private final ClientesRemotos clientes;
    private final TaxisRemotos taxis;
    public GestionViajes(RegistroViajes registro,ClientesRemotos clientes,TaxisRemotos taxis) {
        this.registro=registro;this.clientes=clientes;this.taxis=taxis;
    }
    public synchronized List<Viaje> listar() { return registro.listar(); }
    public synchronized Viaje buscar(long id) { return registro.buscar(id).orElseThrow(()->new ErrorNegocio(NO_EXISTE,"Viaje inexistente")); }
    public synchronized Viaje solicitar(UUID clave,Long cliente,String origen,String destino) {
        var previo=registro.porClave(clave);
        if(previo.isPresent()) {
            if(!previo.get().coincide(cliente,origen,destino)) throw new ErrorNegocio(CONFLICTO,"La clave ya se uso con otra solicitud");
            return previo.get();
        }
        // A technical failure leaves a stored pending request; a missing client is a business error.
        try { clientes.comprobar(cliente); }
        catch(ClienteInexistente e) { throw new ErrorNegocio(NO_EXISTE,"Cliente inexistente"); }
        catch(RuntimeException e) { return registro.crear(clave,nuevaSolicitud(cliente,origen,destino)); }
        var nuevo=registro.crear(clave,nuevaSolicitud(cliente,origen,destino));
        return asignar(nuevo);
    }
    private Viaje nuevaSolicitud(Long cliente,String origen,String destino) {
        // Match SQL timestamp precision so retries return the same timestamp.
        var fecha=LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
        return new Viaje(null,cliente,null,origen,destino,PENDIENTE,fecha);
    }
    public synchronized Viaje reintentar(long id) {
        var v=buscar(id);
        if(v.estado()!=PENDIENTE && v.estado()!=SIN_TAXI_DISPONIBLE) return v;
        try { clientes.comprobar(v.clienteId()); }
        catch(ClienteInexistente e) { throw new ErrorNegocio(NO_EXISTE,"Cliente inexistente"); }
        catch(RuntimeException e) { return registro.guardar(v.cambiar(null,PENDIENTE)); }
        return asignar(v);
    }
    private Viaje asignar(Viaje v) {
        final long taxi;
        try { taxi=taxis.reservar(v.id()); }
        catch(SinTaxi e) { return registro.guardar(v.cambiar(null,SIN_TAXI_DISPONIBLE)); }
        catch(RuntimeException e) { return registro.guardar(v.cambiar(null,PENDIENTE)); }
        return registro.guardar(v.cambiar(taxi,ASIGNADO));
    }
    public synchronized Viaje completar(long id) {
        var v=buscar(id);
        if(v.estado()==COMPLETADO) return v;
        if(v.estado()!=ASIGNADO) throw new ErrorNegocio(CONFLICTO,"El viaje debe estar asignado");
        liberar(v.id());
        return registro.guardar(v.cambiar(v.taxiId(),COMPLETADO));
    }
    public synchronized Viaje cancelar(long id) {
        var v=buscar(id);
        if(v.estado()==CANCELADO) return v;
        if(v.estado()==COMPLETADO) throw new ErrorNegocio(CONFLICTO,"El viaje ya se completo");
        // Release even a pending trip: a previous reservation reply may have been lost.
        liberar(v.id());
        return registro.guardar(v.cambiar(v.taxiId(),CANCELADO));
    }
    private void liberar(long id) {
        try { taxis.liberar(id); }
        catch(RuntimeException e) { throw new ErrorNegocio(NO_DISPONIBLE,"No se confirmo la liberacion; reintente el mismo viaje"); }
    }
}
