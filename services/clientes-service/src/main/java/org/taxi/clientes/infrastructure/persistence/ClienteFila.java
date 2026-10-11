package org.taxi.clientes.infrastructure.persistence;

import jakarta.persistence.*;
import org.taxi.clientes.domain.entities.Cliente;
import org.taxi.clientes.domain.exceptions.ErrorNegocio;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "clientes")
public class ClienteFila {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String nombre;
    @Column(nullable = false, unique = true)
    public String dni;
    @Column(nullable = false)
    public String telefono;
    @Column(nullable = false)
    public String email;
    public ClienteFila() {}
    public Cliente dominio() { return new Cliente(id, nombre, dni, telefono, email); }
    public static ClienteFila de(Cliente valor) {
        var fila = new ClienteFila();
        fila.id = valor.id();
        fila.nombre = valor.nombre();
        fila.dni = valor.dni();
        fila.telefono = valor.telefono();
        fila.email = valor.email();
        return fila;
    }
}
