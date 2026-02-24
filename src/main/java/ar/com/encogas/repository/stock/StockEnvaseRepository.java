package ar.com.encogas.repository.stock;

import ar.com.encogas.domain.stock.StockEnvase;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockEnvaseRepository extends JpaRepository<StockEnvase, Long> {

    List<StockEnvase> findByEmpresa_IdAndPuntoOperativo_IdOrderByTipoEnvase_NombreAscTipoEnvase_CapacidadKgAsc(Long empresaId, Long puntoOperativoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
      select s from StockEnvase s
      where s.empresa.id = :empresaId
        and s.puntoOperativo.id = :poId
        and s.tipoEnvase.id = :tipoId
  """)
    Optional<StockEnvase> findForUpdate(@Param("empresaId") Long empresaId, @Param("poId") Long puntoOperativoId, @Param("tipoId") Long tipoEnvaseId);
}