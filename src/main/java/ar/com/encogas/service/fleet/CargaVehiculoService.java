package ar.com.encogas.service.fleet;

import ar.com.encogas.domain.fleet.CargaVehiculo;
import ar.com.encogas.domain.fleet.DetalleCargaVehiculo;
import ar.com.encogas.domain.fleet.Jornada;
import ar.com.encogas.domain.fleet.JornadaEstado;
import ar.com.encogas.domain.stock.MovimientoStockTipo;
import ar.com.encogas.dto.fleet.CargaVehiculoRequest;
import ar.com.encogas.dto.fleet.CargaVehiculoResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.repository.catalog.TipoEnvaseRepository;
import ar.com.encogas.repository.fleet.CargaVehiculoRepository;
import ar.com.encogas.service.stock.StockService;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CargaVehiculoService {

    private final CurrentUserService currentUser;
    private final JornadaService jornadaService;
    private final CargaVehiculoRepository cargaRepo;
    private final TipoEnvaseRepository tipoEnvaseRepo;
    private final StockService stockService;

    public CargaVehiculoService(
            CurrentUserService currentUser,
            JornadaService jornadaService,
            CargaVehiculoRepository cargaRepo,
            TipoEnvaseRepository tipoEnvaseRepo,
            StockService stockService
    ) {
        this.currentUser = currentUser;
        this.jornadaService = jornadaService;
        this.cargaRepo = cargaRepo;
        this.tipoEnvaseRepo = tipoEnvaseRepo;
        this.stockService = stockService;
    }

    @Transactional(readOnly = true)
    public List<CargaVehiculoResponse> listar(Long jornadaId) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        return cargaRepo.findByEmpresa_IdAndJornada_IdOrderByFechaHoraDesc(empresaId, jornadaId)
                .stream()
                .map(c -> new CargaVehiculoResponse(
                        c.getId(),
                        c.getJornada().getId(),
                        c.getFechaHora(),
                        c.getObservacion(),
                        c.getOrigen().getId(),
                        c.getVehiculoPo().getId(),
                        c.getDetalles().stream()
                                .map(d -> new CargaVehiculoResponse.Item(
                                        d.getTipoEnvase().getId(),
                                        d.getTipoEnvase().getNombre(),
                                        d.getTipoEnvase().getCodigo(),
                                        d.getTipoEnvase().getCapacidadKg().toPlainString(),
                                        d.getCantLlenos(),
                                        d.getCantVacios()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Transactional
    public CargaVehiculoResponse crear(CargaVehiculoRequest req) {
        // Validación empresa / pertenencia está resuelta dentro de JornadaService.getEntity
        Jornada j = jornadaService.getEntity(req.jornadaId());

        if (j.getEstado() != JornadaEstado.ABIERTA) {
            throw new BadRequestException("La jornada está cerrada");
        }

        CargaVehiculo carga = new CargaVehiculo();
        carga.setEmpresa(j.getEmpresa());
        carga.setJornada(j);
        carga.setFechaHora(Instant.now());
        carga.setOrigen(j.getPuntoOperativoOrigen());
        carga.setVehiculoPo(j.getPuntoOperativoVehiculo());
        carga.setObservacion(req.observacion() == null ? null : req.observacion().trim());

        // Construir detalles (ignorando líneas 0/0)
        for (var it : req.items()) {
            if (it.cantLlenos() == 0 && it.cantVacios() == 0) continue;

            var te = tipoEnvaseRepo.findByIdAndEmpresa_Id(it.tipoEnvaseId(), j.getEmpresa().getId())
                    .orElseThrow(() -> new BadRequestException("Tipo envase inválido: " + it.tipoEnvaseId()));

            DetalleCargaVehiculo d = new DetalleCargaVehiculo();
            d.setCargaVehiculo(carga);
            d.setTipoEnvase(te);
            d.setCantLlenos(it.cantLlenos());
            d.setCantVacios(it.cantVacios());
            carga.getDetalles().add(d);
        }

        if (carga.getDetalles().isEmpty()) {
            throw new BadRequestException("Debe incluir al menos un ítem con cantidades > 0");
        }

        // Persistir primero para obtener ID (referencia de movimientos)
        carga = cargaRepo.save(carga);

        // Movimientos de stock por detalle: origen -> vehículo
        String ref = "CARGA#" + carga.getId();
        String motivo = "Carga vehículo " + j.getVehiculo().getPatente() + " (Jornada " + j.getFecha() + ")";

        for (var d : carga.getDetalles()) {
            stockService.transferir(
                    carga.getOrigen().getId(),
                    carga.getVehiculoPo().getId(),
                    d.getTipoEnvase().getId(),
                    d.getCantLlenos(),
                    d.getCantVacios(),
                    MovimientoStockTipo.CARGA_VEHICULO,
                    motivo,
                    ref
            );
        }

        // Respuesta directa (sin lambda que capture variables reasignadas)
        return new CargaVehiculoResponse(
                carga.getId(),
                j.getId(),
                carga.getFechaHora(),
                carga.getObservacion(),
                carga.getOrigen().getId(),
                carga.getVehiculoPo().getId(),
                carga.getDetalles().stream()
                        .map(d -> new CargaVehiculoResponse.Item(
                                d.getTipoEnvase().getId(),
                                d.getTipoEnvase().getNombre(),
                                d.getTipoEnvase().getCodigo(),
                                d.getTipoEnvase().getCapacidadKg().toPlainString(),
                                d.getCantLlenos(),
                                d.getCantVacios()
                        ))
                        .toList()
        );
    }
}