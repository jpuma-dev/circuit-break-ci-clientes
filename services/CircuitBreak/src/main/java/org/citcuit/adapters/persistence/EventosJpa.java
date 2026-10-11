package org.citcuit.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EventosJpa extends JpaRepository<EventoFila,UUID> {
    List<EventoFila> findTop100ByOrderByCreadoAsc();
}
