package ar.com.encogas.service.stock;

import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.TipoEnvase;
import ar.com.encogas.domain.stock.MovimientoStock;
import ar.com.encogas.domain.stock.MovimientoStockTipo;
import ar.com.encogas.domain.stock.StockEnvase;
import ar.com.encogas.dto.stock.*;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.PuntoOperativoRepository;
import ar.com.encogas.repository.catalog.TipoEnvaseRepository;
import ar.com.encogas.repository.stock.MovimientoStockRepository;
import ar.com.encogas.repository.stock.StockEnvaseRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    private final CurrentUserService currentUserService;
    private final StockEnvaseRepository stockRepo;
    private final MovimientoStockRepository movRepo;
    private final PuntoOperativoRepository poRepo;
    private final TipoEnvaseRepository teRepo;

    public StockService(
            CurrentUserService currentUserService,
            StockEnvaseRepository stockRepo,
            MovimientoStockRepository movRepo,
            PuntoOperativoRepository poRepo,
            TipoEnvaseRepository teRepo
    ) {
        this.currentUserService = currentUserService;
        this.stockRepo = stockRepo;
        this.movRepo = movRepo;
        this.poRepo = poRepo;
        this.teRepo = teRepo;
    }

    @Transactional(readOnly = true)
    public List<StockEnvaseRowResponse> stockPorPunto(Long puntoOperativoId) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        // Validar PO pertenece a empresa
        poRepo.findByIdAndEmpresa_Id(puntoOperativoId, empresaId)
                .orElseThrow(() -> new NotFoundException("Punto Operativo no encontrado"));

        return stockRepo.findByEmpresa_IdAndPuntoOperativo_IdOrderByTipoEnvase_NombreAscTipoEnvase_CapacidadKgAsc(empresaId, puntoOperativoId)
                .stream()
                .map(s -> new StockEnvaseRowResponse(
                        s.getTipoEnvase().getId(),
                        s.getTipoEnvase().getCodigo(),
                        s.getTipoEnvase().getNombre(),
                        s.getTipoEnvase().getCapacidadKg().toPlainString(),
                        s.getLlenos(),
                        s.getVacios()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoStockResponse> ultimosMovimientos(Long puntoOperativoId) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        poRepo.findByIdAndEmpresa_Id(puntoOperativoId, empresaId)
                .orElseThrow(() -> new NotFoundException("Punto Operativo no encontrado"));

        return movRepo.findTop50ByEmpresa_IdAndPuntoOperativo_IdOrderByCreatedAtDesc(empresaId, puntoOperativoId)
                .stream()
                .map(m -> new MovimientoStockResponse(
                        m.getId(),
                        m.getTipo(),
                        m.getMotivo(),
                        m.getDeltaLlenos(),
                        m.getDeltaVacios(),
                        m.getSaldoLlenos(),
                        m.getSaldoVacios(),
                        m.getReferencia(),
                        m.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public void ajustar(AjusteStockRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        PuntoOperativo po = poRepo.findByIdAndEmpresa_Id(req.puntoOperativoId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Punto Operativo no encontrado"));

        TipoEnvase te = teRepo.findByIdAndEmpresa_Id(req.tipoEnvaseId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));

        // Bloqueo fila stock (o creamos si no existe)
        StockEnvase stock = stockRepo.findForUpdate(empresaId, po.getId(), te.getId())
                .orElseGet(() -> {
                    StockEnvase s = new StockEnvase();
                    s.setEmpresa(usuario.getEmpresa());
                    s.setPuntoOperativo(po);
                    s.setTipoEnvase(te);
                    s.setLlenos(0);
                    s.setVacios(0);
                    return stockRepo.save(s);
                });

        int nuevoLlenos = req.nuevoLlenos();
        int nuevoVacios = req.nuevoVacios();

        int deltaLlenos = nuevoLlenos - stock.getLlenos();
        int deltaVacios = nuevoVacios - stock.getVacios();

        if (deltaLlenos == 0 && deltaVacios == 0) {
            throw new BadRequestException("El ajuste no genera cambios");
        }

        // aplicar
        stock.setLlenos(nuevoLlenos);
        stock.setVacios(nuevoVacios);
        stockRepo.save(stock);

        // movimiento auditable
        MovimientoStock mov = new MovimientoStock();
        mov.setEmpresa(usuario.getEmpresa());
        mov.setPuntoOperativo(po);
        mov.setTipoEnvase(te);
        mov.setTipo(MovimientoStockTipo.AJUSTE);
        mov.setMotivo(req.motivo().trim());
        mov.setDeltaLlenos(deltaLlenos);
        mov.setDeltaVacios(deltaVacios);
        mov.setSaldoLlenos(nuevoLlenos);
        mov.setSaldoVacios(nuevoVacios);
        mov.setReferencia(null);

        movRepo.save(mov);
    }
}