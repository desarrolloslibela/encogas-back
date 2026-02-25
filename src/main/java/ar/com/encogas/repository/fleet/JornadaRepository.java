package ar.com.encogas.repository.fleet;

import ar.com.encogas.domain.fleet.Jornada;
import ar.com.encogas.domain.fleet.JornadaEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {
    Optional<Jornada> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndFechaAndVehiculo_IdAndEstado(Long empresaId, LocalDate fecha, Long vehiculoId, JornadaEstado estado);

    List<Jornada> findTop50ByEmpresa_IdOrderByFechaDescCreatedAtDesc(Long empresaId);
}