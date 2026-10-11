package org.citcuit.configuration;

import org.citcuit.application.ports.*;
import org.citcuit.application.usecases.*;
import org.springframework.context.annotation.*;
@Configuration
public class CasosConfig {
    @Bean GestionViajes gestionViajes(RegistroViajes r,ClientesRemotos c,TaxisRemotos t) { return new GestionViajes(r,c,t); }
    @Bean DespacharEventos despacharEventos(BuzonEventos b,EmisorEventos e) { return new DespacharEventos(b,e); }
}
