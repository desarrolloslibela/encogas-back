package ar.com.encogas.domain.fleet;

import ar.com.encogas.domain.catalog.TipoEnvase;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_carga_vehiculo")
public class DetalleCargaVehiculo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carga_vehiculo_id", nullable = false)
    private CargaVehiculo cargaVehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_envase_id", nullable = false)
    private TipoEnvase tipoEnvase;

    @Column(name = "cant_llenos", nullable = false)
    private int cantLlenos;

    @Column(name = "cant_vacios", nullable = false)
    private int cantVacios;

    public Long getId() { return id; }

    public CargaVehiculo getCargaVehiculo() { return cargaVehiculo; }
    public void setCargaVehiculo(CargaVehiculo cargaVehiculo) { this.cargaVehiculo = cargaVehiculo; }

    public TipoEnvase getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(TipoEnvase tipoEnvase) { this.tipoEnvase = tipoEnvase; }

    public int getCantLlenos() { return cantLlenos; }
    public void setCantLlenos(int cantLlenos) { this.cantLlenos = cantLlenos; }

    public int getCantVacios() { return cantVacios; }
    public void setCantVacios(int cantVacios) { this.cantVacios = cantVacios; }
}