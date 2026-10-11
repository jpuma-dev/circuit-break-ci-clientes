package org.taxi.taxis.configuration;

import org.springframework.context.annotation.*;
import org.taxi.taxis.application.ports.Taxis;
import org.taxi.taxis.application.usecases.GestionTaxis;
@Configuration
public class CasosConfig { @Bean GestionTaxis gestionTaxis(Taxis taxis) { return new GestionTaxis(taxis); } }
