package ar.com.encogas.domain.sales;

import ar.com.encogas.domain.catalog.TipoEnvase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "venta_item")
public class VentaItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_envase_id", nullable = false)
    private TipoEnvase tipoEnvase;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envase", nullable = false, length = 10)
    private EstadoEnvaseVenta estadoEnvase;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 14, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;

    public Long getId() { return id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public TipoEnvase getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(TipoEnvase tipoEnvase) { this.tipoEnvase = tipoEnvase; }

    public EstadoEnvaseVenta getEstadoEnvase() { return estadoEnvase; }
    public void setEstadoEnvase(EstadoEnvaseVenta estadoEnvase) { this.estadoEnvase = estadoEnvase; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}