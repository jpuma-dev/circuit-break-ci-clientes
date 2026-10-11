package org.taxi.notificaciones.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface NotificacionJpa extends JpaRepository<NotificacionFila, UUID> {
    
}
