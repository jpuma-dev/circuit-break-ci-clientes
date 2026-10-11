package org.taxi.taxis.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface TaxiJpa extends JpaRepository<TaxiFila, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select t from TaxiFila t where t.estado = :estado order by t.id")
    java.util.List<TaxiFila> disponibles(@org.springframework.data.repository.query.Param("estado") org.taxi.taxis.domain.EstadoTaxi estado);
}
