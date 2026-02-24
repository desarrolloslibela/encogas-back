package ar.com.encogas.repository.fleet;

import ar.com.encogas.domain.fleet.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndPatenteIgnoreCase(Long empresaId, String patente);
    boolean existsByEmpresa_IdAndPatenteIgnoreCaseAndIdNot(Long empresaId, String patente, Long id);

    List<Vehiculo> findByEmpresa_IdAndActivoOrderByPatenteAsc(Long empresaId, boolean activo);
    List<Vehiculo> findByEmpresa_IdOrderByPatenteAsc(Long empresaId);
}