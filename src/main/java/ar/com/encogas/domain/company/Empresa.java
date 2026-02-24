package ar.com.encogas.domain.company;

import ar.com.encogas.domain.audit.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "empresa")
public class Empresa extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}