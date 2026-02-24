package ar.com.encogas.repository.stock;

import ar.com.encogas.domain.stock.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    List<MovimientoStock> findTop50ByEmpresa_IdAndPuntoOperativo_IdOrderByCreatedAtDesc(Long empresaId, Long puntoOperativoId);
}