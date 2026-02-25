package ar.com.encogas.domain.fleet;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.company.Empresa;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carga_vehiculo")
public class CargaVehiculo extends AuditableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_origen_id", nullable = false)
    private PuntoOperativo origen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_vehiculo_id", nullable = false)
    private PuntoOperativo vehiculoPo;

    @Column(length = 255)
    private String observacion;

    @OneToMany(mappedBy = "cargaVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCargaVehiculo> detalles = new ArrayList<>();

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Jornada getJornada() { return jornada; }
    public void setJornada(Jornada jornada) { this.jornada = jornada; }

    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }

    public PuntoOperativo getOrigen() { return origen; }
    public void setOrigen(PuntoOperativo origen) { this.origen = origen; }

    public PuntoOperativo getVehiculoPo() { return vehiculoPo; }
    public void setVehiculoPo(PuntoOperativo vehiculoPo) { this.vehiculoPo = vehiculoPo; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public List<DetalleCargaVehiculo> getDetalles() { return detalles; }
}