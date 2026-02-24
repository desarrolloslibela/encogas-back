package ar.com.encogas.domain.catalog;

import ar.com.encogas.domain.audit.AuditableEntity;
import ar.com.encogas.domain.company.Empresa;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cliente")
public class Cliente extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "cuit_dni", length = 20)
    private String cuitDni;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "localidad", length = 120)
    private String localidad;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "email", length = 190)
    private String email;

    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    public Long getId() { return id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getCuitDni() { return cuitDni; }
    public void setCuitDni(String cuitDni) { this.cuitDni = cuitDni; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}