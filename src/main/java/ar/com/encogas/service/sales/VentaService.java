package ar.com.encogas.service.sales;

import ar.com.encogas.domain.fleet.Jornada;
import ar.com.encogas.domain.fleet.JornadaEstado;
import ar.com.encogas.domain.sales.EstadoEnvaseVenta;
import ar.com.encogas.domain.sales.Venta;
import ar.com.encogas.domain.sales.VentaItem;
import ar.com.encogas.domain.sales.VentaMetodoPago;
import ar.com.encogas.domain.stock.MovimientoStockTipo;
import ar.com.encogas.dto.sales.VentaCreateRequest;
import ar.com.encogas.dto.sales.VentaResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.TipoEnvaseRepository;
import ar.com.encogas.repository.catalog.ClienteRepository;
import ar.com.encogas.repository.sales.VentaRepository;
import ar.com.encogas.security.CurrentUserService;
import ar.com.encogas.service.fleet.JornadaService;
import ar.com.encogas.service.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class VentaService {

    private final CurrentUserService currentUser;
    private final JornadaService jornadaService;
    private final ClienteRepository clienteRepo;
    private final TipoEnvaseRepository tipoEnvaseRepo;
    private final VentaRepository ventaRepo;
    private final StockService stockService;

    public VentaService(
            CurrentUserService currentUser,
            JornadaService jornadaService,
            ClienteRepository clienteRepo,
            TipoEnvaseRepository tipoEnvaseRepo,
            VentaRepository ventaRepo,
            StockService stockService
    ) {
        this.currentUser = currentUser;
        this.jornadaService = jornadaService;
        this.clienteRepo = clienteRepo;
        this.tipoEnvaseRepo = tipoEnvaseRepo;
        this.ventaRepo = ventaRepo;
        this.stockService = stockService;
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> ultimasPorJornada(Long jornadaId) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        return ventaRepo.findTop50ByEmpresa_IdAndJornada_IdOrderByFechaHoraDesc(empresaId, jornadaId)
                .stream()
                .map(this::toResp)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse get(Long id) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Venta v = ventaRepo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        return toResp(v);
    }

    @Transactional
    public VentaResponse crear(VentaCreateRequest req) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Jornada j = jornadaService.getEntity(req.jornadaId());
        if (j.getEstado() != JornadaEstado.ABIERTA) {
            throw new BadRequestException("La jornada está cerrada");
        }

        var cliente = clienteRepo.findByIdAndEmpresa_Id(req.clienteId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        // Validaciones items
        if (req.items() == null || req.items().isEmpty()) {
            throw new BadRequestException("Debe incluir al menos un ítem");
        }

        Venta venta = new Venta();
        venta.setEmpresa(u.getEmpresa());
        venta.setJornada(j);
        venta.setCliente(cliente);
        venta.setFechaHora(Instant.now());
        venta.setMetodoPago(req.metodoPago());
        venta.setObservacion(req.observacion() == null ? null : req.observacion().trim());

        BigDecimal total = BigDecimal.ZERO;

        for (var it : req.items()) {
            if (it.cantidad() <= 0) throw new BadRequestException("Cantidad inválida");
            if (it.precioUnitario() == null) throw new BadRequestException("Precio unitario requerido");
            if (it.precioUnitario().compareTo(BigDecimal.ZERO) < 0) throw new BadRequestException("Precio unitario inválido");
            if (it.estadoEnvase() == null) throw new BadRequestException("Estado envase requerido");

            var te = tipoEnvaseRepo.findByIdAndEmpresa_Id(it.tipoEnvaseId(), empresaId)
                    .orElseThrow(() -> new BadRequestException("Tipo envase inválido: " + it.tipoEnvaseId()));

            BigDecimal precio = it.precioUnitario().setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(it.cantidad())).setScale(2, RoundingMode.HALF_UP);

            VentaItem vi = new VentaItem();
            vi.setVenta(venta);
            vi.setTipoEnvase(te);
            vi.setEstadoEnvase(it.estadoEnvase());
            vi.setCantidad(it.cantidad());
            vi.setPrecioUnitario(precio);
            vi.setSubtotal(subtotal);

            venta.getItems().add(vi);
            total = total.add(subtotal);
        }

        total = total.setScale(2, RoundingMode.HALF_UP);

        // Cobranza / saldo deuda
        BigDecimal cobrado = req.montoCobrado() == null ? BigDecimal.ZERO : req.montoCobrado().setScale(2, RoundingMode.HALF_UP);
        if (cobrado.compareTo(BigDecimal.ZERO) < 0) throw new BadRequestException("Monto cobrado inválido");
        if (cobrado.compareTo(total) > 0) throw new BadRequestException("Monto cobrado no puede superar el total");

        // Reglas por método de pago
        VentaMetodoPago mp = req.metodoPago();
        if (mp == VentaMetodoPago.DEUDA && cobrado.compareTo(BigDecimal.ZERO) != 0) {
            throw new BadRequestException("Si el método es DEUDA, el monto cobrado debe ser 0");
        }
        if ((mp == VentaMetodoPago.EFECTIVO || mp == VentaMetodoPago.TRANSFERENCIA) && cobrado.compareTo(total) != 0) {
            throw new BadRequestException("Si el método es EFECTIVO/TRANSFERENCIA, el monto cobrado debe ser igual al total");
        }
        if (mp == VentaMetodoPago.MIXTO) {
            if (cobrado.compareTo(BigDecimal.ZERO) <= 0 || cobrado.compareTo(total) >= 0) {
                throw new BadRequestException("Si el método es MIXTO, el monto cobrado debe ser mayor a 0 y menor al total");
            }
        }

        BigDecimal saldo = total.subtract(cobrado).setScale(2, RoundingMode.HALF_UP);

        venta.setTotalVenta(total);
        venta.setMontoCobrado(cobrado);
        venta.setSaldoDeuda(saldo);

        // Guardar venta para tener ID (referencia stock)
        venta = ventaRepo.save(venta);

        // Impacto stock: del vehículo hacia "afuera" (solo decremento en el PO del vehículo)
        Long poVehiculoId = j.getPuntoOperativoVehiculo().getId();
        String ref = "VENTA#" + venta.getId();
        String motivo = "Venta a " + cliente.getRazonSocial() + " (Jornada " + j.getFecha() + ", " + j.getVehiculo().getPatente() + ")";

        for (var vi : venta.getItems()) {
            int cant = vi.getCantidad();
            Long tipoEnvaseId = vi.getTipoEnvase().getId();

            int deltaLlenos = 0;
            int deltaVacios = 0;

            if (vi.getEstadoEnvase() == EstadoEnvaseVenta.LLENO) deltaLlenos = cant;
            if (vi.getEstadoEnvase() == EstadoEnvaseVenta.VACIO) deltaVacios = cant;

            // usamos el mismo transferir pero enviando a un "destino virtual" no tiene sentido.
            // entonces hacemos un ajuste SOLO ORIGEN:
            stockService.ajustarDelta(
                    poVehiculoId,
                    tipoEnvaseId,
                    -deltaLlenos,
                    -deltaVacios,
                    MovimientoStockTipo.VENTA,
                    motivo,
                    ref
            );
        }

        return toResp(venta);
    }

    private VentaResponse toResp(Venta v) {
        return new VentaResponse(
                v.getId(),
                v.getJornada().getId(),
                v.getCliente().getId(),
                v.getCliente().getRazonSocial(),
                v.getFechaHora(),
                v.getMetodoPago(),
                v.getMontoCobrado(),
                v.getTotalVenta(),
                v.getSaldoDeuda(),
                v.getObservacion(),
                v.getItems().stream().map(it -> new VentaResponse.Item(
                        it.getTipoEnvase().getId(),
                        it.getTipoEnvase().getNombre(),
                        it.getTipoEnvase().getCodigo(),
                        it.getTipoEnvase().getCapacidadKg().toPlainString(),
                        it.getEstadoEnvase().name(),
                        it.getCantidad(),
                        it.getPrecioUnitario(),
                        it.getSubtotal()
                )).toList()
        );
    }
}