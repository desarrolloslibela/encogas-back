package ar.com.encogas.repository.company;

import ar.com.encogas.domain.company.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByNombreIgnoreCase(String nombre);
}