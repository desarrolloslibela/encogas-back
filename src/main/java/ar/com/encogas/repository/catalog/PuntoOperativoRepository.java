package ar.com.encogas.repository.catalog;

import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PuntoOperativoRepository extends JpaRepository<PuntoOperativo, Long> {

    Optional<PuntoOperativo> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndCodigoIgnoreCase(Long empresaId, String codigo);

    boolean existsByEmpresa_IdAndCodigoIgnoreCaseAndIdNot(Long empresaId, String codigo, Long id);

    boolean existsByEmpresa_IdAndTipoAndActivoTrue(Long empresaId, PuntoOperativoTipo tipo);

    List<PuntoOperativo> findByEmpresa_IdAndActivoOrderByNombreAsc(Long empresaId, boolean activo);

    List<PuntoOperativo> findByEmpresa_IdOrderByNombreAsc(Long empresaId);
}