package org.taxi.clientes.presentation.controllers;

import org.taxi.clientes.application.usecases.GestionClientes;
import org.taxi.clientes.application.usecases.ListarClientesUseCase;
import org.taxi.clientes.presentation.dto.ClienteRequest;
import org.taxi.clientes.domain.entities.Cliente;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
@RestController
@RequestMapping("/api/clientes")
public class ClientesHttp {
    private final GestionClientes casos;
    private final ListarClientesUseCase listado;
    public ClientesHttp(GestionClientes casos, ListarClientesUseCase listado) {
        this.casos = casos;
        this.listado = listado;
    }
    @GetMapping public List<Cliente> listar() { return listado.ejecutar(); }
    @GetMapping("/{id}") public Cliente buscar(@PathVariable long id) { return casos.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody ClienteRequest e) { return casos.guardar(null,e.nombre(),e.dni(),e.telefono(),e.email()); }
    @PutMapping("/{id}")
    public Cliente editar(@PathVariable long id,@Valid @RequestBody ClienteRequest e) { return casos.guardar(id,e.nombre(),e.dni(),e.telefono(),e.email()); }
}
