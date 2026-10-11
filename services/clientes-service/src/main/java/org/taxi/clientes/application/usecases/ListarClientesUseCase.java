package org.taxi.clientes.application.usecases;

import java.util.List;
import org.taxi.clientes.application.ports.Clientes;
import org.taxi.clientes.domain.entities.Cliente;

public class ListarClientesUseCase {
    private final Clientes clientes;

    public ListarClientesUseCase(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<Cliente> ejecutar() {
        return clientes.listar();
    }
}
