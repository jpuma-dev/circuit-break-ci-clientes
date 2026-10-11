package org.taxi.clientes.application.ports;

import org.taxi.clientes.domain.entities.Cliente;
import java.util.*;
public interface Clientes {
    List<Cliente> listar();
    Optional<Cliente> buscar(long id);
    Cliente guardar(Cliente cliente);
    boolean dniOcupado(String dni, long exceptoId);
}
