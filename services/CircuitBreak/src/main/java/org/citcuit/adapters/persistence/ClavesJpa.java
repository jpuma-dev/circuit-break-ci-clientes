package org.citcuit.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ClavesJpa extends JpaRepository<ClaveFila,UUID> {}
