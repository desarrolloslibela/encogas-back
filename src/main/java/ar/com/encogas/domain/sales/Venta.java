package ar.com.encogas.domain.sales;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.company.Empresa;
import ar.com.encogas.domain.fleet.Jornada;
import ar.com.encogas.domain.catalog.Cliente;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta extends AuditableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private VentaMetodoPago metodoPago;

    @Column(name = "monto_cobrado", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoCobrado;

    @Column(name = "total_venta", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalVenta;

    @Column(name = "saldo_deuda", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoDeuda;

    @Column(length = 255)
    private String observacion;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaItem> items = new ArrayList<>();

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Jornada getJornada() { return jornada; }
    public void setJornada(Jornada jornada) { this.jornada = jornada; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }

    public VentaMetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(VentaMetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getMontoCobrado() { return montoCobrado; }
    public void setMontoCobrado(BigDecimal montoCobrado) { this.montoCobrado = montoCobrado; }

    public BigDecimal getTotalVenta() { return totalVenta; }
    public void setTotalVenta(BigDecimal totalVenta) { this.totalVenta = totalVenta; }

    public BigDecimal getSaldoDeuda() { return saldoDeuda; }
    public void setSaldoDeuda(BigDecimal saldoDeuda) { this.saldoDeuda = saldoDeuda; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public List<VentaItem> getItems() { return items; }
}