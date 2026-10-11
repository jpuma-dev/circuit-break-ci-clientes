package org.taxi.clientes.infrastructure.persistence;

import org.taxi.clientes.domain.entities.Cliente;
import org.taxi.clientes.application.ports.Clientes;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class ClientesH2 implements Clientes {
    private final ClienteJpa jpa;
    public ClientesH2(ClienteJpa jpa) { this.jpa = jpa; }
    public List<Cliente> listar() { return jpa.findAll().stream().map(ClienteFila::dominio).toList(); }
    public Optional<Cliente> buscar(long id) { return jpa.findById(id).map(ClienteFila::dominio); }
    public Cliente guardar(Cliente c) { return jpa.saveAndFlush(ClienteFila.de(c)).dominio(); }
    public boolean dniOcupado(String dni, long exceptoId) { return jpa.existsByDniAndIdNot(dni, exceptoId); }
}
