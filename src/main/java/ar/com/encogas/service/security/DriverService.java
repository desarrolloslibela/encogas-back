package ar.com.encogas.service.security;

import ar.com.encogas.dto.security.DriverResponse;
import ar.com.encogas.security.CurrentUserService;
import ar.com.encogas.domain.security.Usuario;
import ar.com.encogas.repository.security.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.com.encogas.domain.security.Rol;


import java.util.List;

@Service
public class DriverService {

    private final UsuarioRepository usuarioRepo;
    private final CurrentUserService currentUser;

    public DriverService(UsuarioRepository usuarioRepo, CurrentUserService currentUser) {
        this.usuarioRepo = usuarioRepo;
        this.currentUser = currentUser;
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> listDrivers() {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        // Asumo que Usuario tiene roles como strings/enum. Ajustá el método del repo si tu modelo difiere.
        //List<Usuario> drivers = usuarioRepo.findByEmpresa_IdAndRolesContainsOrderByEmailAsc(empresaId, "DRIVER");

        List<Usuario> drivers = usuarioRepo.findByEmpresaIdAndRol(empresaId, Rol.DRIVER);
        return drivers.stream()
                .map(d -> new DriverResponse(d.getId(), d.getEmail(), (d.getNombre() + " " + d.getApellido()).trim()))
                .toList();
    }
}