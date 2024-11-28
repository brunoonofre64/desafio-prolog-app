package com.prologapp.desafio.insfraestructure.persistence.jpa;

import com.prologapp.desafio.domain.entities.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaVeiculoRepository extends JpaRepository<VeiculoEntity, Long> {
    Optional<VeiculoEntity> findVeiculoEntityByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCase(String placa);
}
