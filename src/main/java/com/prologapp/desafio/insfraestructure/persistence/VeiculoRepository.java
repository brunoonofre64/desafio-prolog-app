package com.prologapp.desafio.insfraestructure.persistence;

import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.repositories.IVeiculoRepository;
import com.prologapp.desafio.insfraestructure.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.StringJoiner;

@Repository
@AllArgsConstructor
public class VeiculoRepository implements IVeiculoRepository  {
    private static final String DELIMITER = " ";
    private EntityManager entityManager;

    @Override
    public List<VeiculoEntity> obterListaVeiculosSemPneus(int offset, int size) {
        try {
            StringJoiner query = new StringJoiner(DELIMITER)
                    .add("SELECT *")
                    .add("FROM")
                    .add("Veiculo")
                    .add("ORDER BY placa DESC")
                    .add("LIMIT :size OFFSET :offset");

            return entityManager.createNativeQuery(query.toString(), VeiculoEntity.class)
                    .setParameter("offset", offset)
                    .setParameter("size", size)
                    .getResultList();
        } catch (Throwable ex) {
            throw new RepositoryException(CodigoErro.ERRO_AO_BUSCAR_ELEMENTOS,
                    VeiculoRepository.class.getName());
        }
     }

    @Override
    public Long contaTotalRegistrosVeiculo() {
        try {
            String query = "SELECT COUNT(*) FROM veiculo";
            return ((Number) entityManager.createNativeQuery(query).getSingleResult()).longValue();
        } catch (Throwable ex) {
            throw new RepositoryException(CodigoErro.ERRO_AO_CONTAR_ELEMENTOS,
                    VeiculoRepository.class.getName());
        }
    }
}
