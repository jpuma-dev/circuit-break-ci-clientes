package org.taxi.clientes.application.usecases;

import org.taxi.clientes.domain.entities.Cliente;
import org.taxi.clientes.domain.exceptions.ErrorNegocio;
import org.taxi.clientes.application.ports.Clientes;
import static org.taxi.clientes.domain.exceptions.ErrorNegocio.Tipo.*;
public class GestionClientes {
    private final Clientes clientes;
    public GestionClientes(Clientes clientes) { this.clientes = clientes; }
    public Cliente buscar(long id) { return clientes.buscar(id).orElseThrow(() -> new ErrorNegocio(NO_EXISTE, "Cliente inexistente")); }
    public synchronized Cliente guardar(Long id, String nombre, String dni, String telefono, String email) {
        if (id != null) buscar(id);
        if (clientes.dniOcupado(dni, id == null ? 0 : id)) throw new ErrorNegocio(CONFLICTO, "DNI ya registrado");
        return clientes.guardar(new Cliente(id, nombre, dni, telefono, email));
    }
}
