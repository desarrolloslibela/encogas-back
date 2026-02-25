package ar.com.encogas.domain.fleet;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.company.Empresa;
import ar.com.encogas.domain.security.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "jornada")
public class Jornada extends AuditableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chofer_usuario_id", nullable = false)
    private Usuario chofer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JornadaEstado estado = JornadaEstado.ABIERTA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_origen_id", nullable = false)
    private PuntoOperativo puntoOperativoOrigen; // Casa Central

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_operativo_vehiculo_id", nullable = false)
    private PuntoOperativo puntoOperativoVehiculo; // PO VEHICULO

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Usuario getChofer() { return chofer; }
    public void setChofer(Usuario chofer) { this.chofer = chofer; }

    public JornadaEstado getEstado() { return estado; }
    public void setEstado(JornadaEstado estado) { this.estado = estado; }

    public PuntoOperativo getPuntoOperativoOrigen() { return puntoOperativoOrigen; }
    public void setPuntoOperativoOrigen(PuntoOperativo puntoOperativoOrigen) { this.puntoOperativoOrigen = puntoOperativoOrigen; }

    public PuntoOperativo getPuntoOperativoVehiculo() { return puntoOperativoVehiculo; }
    public void setPuntoOperativoVehiculo(PuntoOperativo puntoOperativoVehiculo) { this.puntoOperativoVehiculo = puntoOperativoVehiculo; }
}