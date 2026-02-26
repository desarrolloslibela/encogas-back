package ar.com.encogas.repository.sales;

import ar.com.encogas.domain.sales.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByIdAndEmpresa_Id(Long id, Long empresaId);
    List<Venta> findTop50ByEmpresa_IdAndJornada_IdOrderByFechaHoraDesc(Long empresaId, Long jornadaId);
}