package org.taxi.clientes.infrastructure.config;

import org.springframework.context.annotation.*;
import org.taxi.clientes.application.ports.Clientes;
import org.taxi.clientes.application.usecases.GestionClientes;
import org.taxi.clientes.application.usecases.ListarClientesUseCase;
@Configuration
public class CasosConfig {
    @Bean GestionClientes gestionClientes(Clientes clientes) { return new GestionClientes(clientes); }
    @Bean ListarClientesUseCase listarClientes(Clientes clientes) { return new ListarClientesUseCase(clientes); }
}
