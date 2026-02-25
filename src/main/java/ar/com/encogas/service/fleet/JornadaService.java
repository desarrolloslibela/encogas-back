package ar.com.encogas.service.fleet;

import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.fleet.*;
import ar.com.encogas.domain.security.Usuario;
import ar.com.encogas.dto.fleet.JornadaCreateRequest;
import ar.com.encogas.dto.fleet.JornadaResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.PuntoOperativoRepository;
import ar.com.encogas.repository.fleet.JornadaRepository;
import ar.com.encogas.repository.fleet.VehiculoRepository;
import ar.com.encogas.repository.security.UsuarioRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.com.encogas.domain.security.Rol;


import java.util.List;

@Service
public class JornadaService {

    private final CurrentUserService currentUser;
    private final JornadaRepository jornadaRepo;
    private final VehiculoRepository vehiculoRepo;
    private final UsuarioRepository usuarioRepo;
    private final PuntoOperativoRepository poRepo;

    public JornadaService(CurrentUserService currentUser, JornadaRepository jornadaRepo, VehiculoRepository vehiculoRepo,
                          UsuarioRepository usuarioRepo, PuntoOperativoRepository poRepo) {
        this.currentUser = currentUser;
        this.jornadaRepo = jornadaRepo;
        this.vehiculoRepo = vehiculoRepo;
        this.usuarioRepo = usuarioRepo;
        this.poRepo = poRepo;
    }

    @Transactional(readOnly = true)
    public List<JornadaResponse> ultimas() {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        return jornadaRepo.findTop50ByEmpresa_IdOrderByFechaDescCreatedAtDesc(empresaId)
                .stream().map(this::toResp).toList();
    }

    @Transactional
    public JornadaResponse crear(JornadaCreateRequest req) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Vehiculo vehiculo = vehiculoRepo.findByIdAndEmpresa_Id(req.vehiculoId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Vehículo no encontrado"));

        Usuario chofer = usuarioRepo.findByIdAndEmpresa_Id(req.choferUsuarioId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Chofer no encontrado"));

        // Validación rol DRIVER (mínima)
        if (!usuarioRepo.userHasRole(chofer.getId(), Rol.DRIVER)) {
            throw new BadRequestException("El usuario seleccionado no tiene rol DRIVER");
        }

        PuntoOperativo origen = poRepo.findByIdAndEmpresa_Id(req.puntoOperativoOrigenId(), empresaId)
                .orElseThrow(() -> new NotFoundException("Punto de origen no encontrado"));

        // Regla: 1 jornada abierta por (fecha, vehículo)
        if (jornadaRepo.existsByEmpresa_IdAndFechaAndVehiculo_IdAndEstado(empresaId, req.fecha(), vehiculo.getId(), JornadaEstado.ABIERTA)) {
            throw new BadRequestException("Ya existe una jornada ABIERTA para ese vehículo y fecha");
        }

        Jornada j = new Jornada();
        j.setEmpresa(u.getEmpresa());
        j.setFecha(req.fecha());
        j.setVehiculo(vehiculo);
        j.setChofer(chofer);
        j.setEstado(JornadaEstado.ABIERTA);
        j.setPuntoOperativoOrigen(origen);
        j.setPuntoOperativoVehiculo(vehiculo.getPuntoOperativo());

        return toResp(jornadaRepo.save(j));
    }

    @Transactional
    public void cerrar(Long jornadaId) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Jornada j = jornadaRepo.findByIdAndEmpresa_Id(jornadaId, empresaId)
                .orElseThrow(() -> new NotFoundException("Jornada no encontrada"));

        if (j.getEstado() == JornadaEstado.CERRADA) return;

        j.setEstado(JornadaEstado.CERRADA);
        jornadaRepo.save(j);
    }

    @Transactional(readOnly = true)
    public Jornada getEntity(Long jornadaId) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();
        return jornadaRepo.findByIdAndEmpresa_Id(jornadaId, empresaId)
                .orElseThrow(() -> new NotFoundException("Jornada no encontrada"));
    }

    private JornadaResponse toResp(Jornada j) {
        return new JornadaResponse(
                j.getId(),
                j.getFecha(),
                j.getEstado(),
                j.getVehiculo().getId(),
                j.getVehiculo().getPatente(),
                j.getChofer().getId(),
                j.getChofer().getEmail(),
                j.getPuntoOperativoOrigen().getId(),
                j.getPuntoOperativoOrigen().getNombre(),
                j.getPuntoOperativoVehiculo().getId()
        );
    }
}