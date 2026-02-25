package ar.com.encogas.repository.fleet;

import ar.com.encogas.domain.fleet.CargaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargaVehiculoRepository extends JpaRepository<CargaVehiculo, Long> {
    List<CargaVehiculo> findByEmpresa_IdAndJornada_IdOrderByFechaHoraDesc(Long empresaId, Long jornadaId);
}