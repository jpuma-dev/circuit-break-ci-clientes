package org.taxi.notificaciones.domain;

import java.util.UUID;
public record Notificacion(UUID eventoId, Long viajeId, String contenido) {}
