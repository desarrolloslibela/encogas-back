package ar.com.encogas.domain.stock;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.TipoEnvase;
import ar.com.encogas.domain.company.Empresa;
import jakarta.persistence.*;

@Entity
@Table(name = "stock_envase")
public class StockEnvase extends AuditableEntity {

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

    @Column(nullable = false)
    private int llenos = 0;

    @Column(nullable = false)
    private int vacios = 0;

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public PuntoOperativo getPuntoOperativo() { return puntoOperativo; }
    public void setPuntoOperativo(PuntoOperativo puntoOperativo) { this.puntoOperativo = puntoOperativo; }

    public TipoEnvase getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(TipoEnvase tipoEnvase) { this.tipoEnvase = tipoEnvase; }

    public int getLlenos() { return llenos; }
    public void setLlenos(int llenos) { this.llenos = llenos; }

    public int getVacios() { return vacios; }
    public void setVacios(int vacios) { this.vacios = vacios; }
}