package ar.com.encogas.domain.fleet;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.company.Empresa;
import jakarta.persistence.*;

@Entity
@Table(name = "vehiculo")
public class Vehiculo extends AuditableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_id", nullable = false)
    private PuntoOperativo puntoOperativo;

    @Column(nullable = false, length = 15)
    private String patente;

    @Column(nullable = false)
    private boolean activo = true;

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public PuntoOperativo getPuntoOperativo() { return puntoOperativo; }
    public void setPuntoOperativo(PuntoOperativo puntoOperativo) { this.puntoOperativo = puntoOperativo; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}