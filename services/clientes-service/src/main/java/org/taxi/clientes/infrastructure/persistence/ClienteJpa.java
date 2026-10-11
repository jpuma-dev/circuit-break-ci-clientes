package org.taxi.clientes.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ClienteJpa extends JpaRepository<ClienteFila, Long> {
    boolean existsByDniAndIdNot(String dni, Long id);
}
