package ar.com.encogas.domain.stock;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.TipoEnvase;
import ar.com.encogas.domain.company.Empresa;
import jakarta.persistence.*;

@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_id", nullable = false)
    private PuntoOperativo puntoOperativo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_envase_id", nullable = false)
    private TipoEnvase tipoEnvase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MovimientoStockTipo tipo;

    @Column(length = 255)
    private String motivo;

    @Column(name = "delta_llenos", nullable = false)
    private int deltaLlenos;

    @Column(name = "delta_vacios", nullable = false)
    private int deltaVacios;

    @Column(name = "saldo_llenos", nullable = false)
    private int saldoLlenos;

    @Column(name = "saldo_vacios", nullable = false)
    private int saldoVacios;

    @Column(length = 60)
    private String referencia;

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public PuntoOperativo getPuntoOperativo() { return puntoOperativo; }
    public void setPuntoOperativo(PuntoOperativo puntoOperativo) { this.puntoOperativo = puntoOperativo; }

    public TipoEnvase getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(TipoEnvase tipoEnvase) { this.tipoEnvase = tipoEnvase; }

    public MovimientoStockTipo getTipo() { return tipo; }
    public void setTipo(MovimientoStockTipo tipo) { this.tipo = tipo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public int getDeltaLlenos() { return deltaLlenos; }
    public void setDeltaLlenos(int deltaLlenos) { this.deltaLlenos = deltaLlenos; }

    public int getDeltaVacios() { return deltaVacios; }
    public void setDeltaVacios(int deltaVacios) { this.deltaVacios = deltaVacios; }

    public int getSaldoLlenos() { return saldoLlenos; }
    public void setSaldoLlenos(int saldoLlenos) { this.saldoLlenos = saldoLlenos; }

    public int getSaldoVacios() { return saldoVacios; }
    public void setSaldoVacios(int saldoVacios) { this.saldoVacios = saldoVacios; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}