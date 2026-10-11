package org.citcuit.application.ports;

import org.citcuit.domain.Viaje;
import java.util.*;
public interface RegistroViajes {
    Optional<Viaje> porClave(UUID clave);
    Optional<Viaje> buscar(long id);
    List<Viaje> listar();
    Viaje crear(UUID clave,Viaje viaje);
    Viaje guardar(Viaje viaje);
}
