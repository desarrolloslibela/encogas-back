package ar.com.encogas.repository.security;

import ar.com.encogas.domain.security.Rol;
import ar.com.encogas.domain.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // --- Lo tuyo, intacto ---
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    Optional<Usuario> findByIdAndEmpresa_Id(Long id, Long empresaId);

    // --- Roles: FIX ---
    // Reemplaza el derivado (rolesContains + String) por JPQL correcto con Enum Rol
    @Query("""
        select u
        from Usuario u
        where u.empresa.id = :empresaId
          and :rol member of u.roles
        order by u.email asc
    """)
    List<Usuario> findByEmpresaIdAndRol(@Param("empresaId") Long empresaId, @Param("rol") Rol rol);

    @Query("""
        select (count(u) > 0)
        from Usuario u
        where u.id = :userId
          and :rol member of u.roles
    """)
    boolean userHasRole(@Param("userId") Long userId, @Param("rol") Rol rol);
}