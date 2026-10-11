package org.taxi.notificaciones.configuration;

import org.taxi.notificaciones.application.ports.Notificaciones;
import org.taxi.notificaciones.application.usecases.RecibirNotificaciones;
import org.springframework.context.annotation.*;
@Configuration
public class CasosConfig {
    @Bean RecibirNotificaciones recibirNotificaciones(Notificaciones n) { return new RecibirNotificaciones(n); }
}
