package ar.com.encogas.repository.catalog;

import ar.com.encogas.domain.catalog.TipoEnvase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TipoEnvaseRepository extends JpaRepository<TipoEnvase, Long> {

    Optional<TipoEnvase> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndCodigoIgnoreCase(Long empresaId, String codigo);

    boolean existsByEmpresa_IdAndCodigoIgnoreCaseAndIdNot(Long empresaId, String codigo, Long id);

    boolean existsByEmpresa_IdAndNombreIgnoreCaseAndCapacidadKg(Long empresaId, String nombre, BigDecimal capacidadKg);

    boolean existsByEmpresa_IdAndNombreIgnoreCaseAndCapacidadKgAndIdNot(Long empresaId, String nombre, BigDecimal capacidadKg, Long id);

    List<TipoEnvase> findByEmpresa_IdOrderByNombreAscCapacidadKgAsc(Long empresaId);

    List<TipoEnvase> findByEmpresa_IdAndActivoOrderByNombreAscCapacidadKgAsc(Long empresaId, boolean activo);
}