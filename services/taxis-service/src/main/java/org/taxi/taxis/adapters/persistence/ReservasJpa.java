package org.taxi.taxis.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ReservasJpa extends JpaRepository<ReservaFila,Long> { boolean existsByTaxiId(Long taxiId); }
