package com.prologapp.desafio.insfraestructure.persistence.jpa;

import com.prologapp.desafio.domain.entities.PneuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPneuRepository extends JpaRepository<PneuEntity, Long> {
    boolean existsByNumeroFogo(Long numeroFogo);

    Optional<PneuEntity> findByNumeroFogo(Long numeroFogo);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM PneuEntity p " +
            "WHERE p.numeroFogo = :numeroFogo AND p.veiculo IS NOT NULL")
    boolean existsByNumeroFogoAndVeiculoNotNull(@Param("numeroFogo") Long numeroFogo);
}
