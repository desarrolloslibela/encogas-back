package ar.com.encogas.repository.catalog;

import ar.com.encogas.domain.catalog.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndCuitDniIgnoreCase(Long empresaId, String cuitDni);

    boolean existsByEmpresa_IdAndCuitDniIgnoreCaseAndIdNot(Long empresaId, String cuitDni, Long id);

    List<Cliente> findByEmpresa_IdOrderByRazonSocialAsc(Long empresaId);

    List<Cliente> findByEmpresa_IdAndActivoOrderByRazonSocialAsc(Long empresaId, boolean activo);
}