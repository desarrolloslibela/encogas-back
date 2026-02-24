package ar.com.encogas.service.fleet;

import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import ar.com.encogas.domain.fleet.Vehiculo;
import ar.com.encogas.dto.fleet.VehiculoRequest;
import ar.com.encogas.dto.fleet.VehiculoResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.PuntoOperativoRepository;
import ar.com.encogas.repository.fleet.VehiculoRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {

    public enum Estado { ACTIVOS, INACTIVOS, TODOS }

    private final VehiculoRepository repo;
    private final PuntoOperativoRepository poRepo;
    private final CurrentUserService currentUser;

    public VehiculoService(VehiculoRepository repo, PuntoOperativoRepository poRepo, CurrentUserService currentUser) {
        this.repo = repo;
        this.poRepo = poRepo;
        this.currentUser = currentUser;
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponse> list(Estado estado) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        if (estado == null) estado = Estado.ACTIVOS;

        List<Vehiculo> base = switch (estado) {
            case TODOS -> repo.findByEmpresa_IdOrderByPatenteAsc(empresaId);
            case INACTIVOS -> repo.findByEmpresa_IdAndActivoOrderByPatenteAsc(empresaId, false);
            default -> repo.findByEmpresa_IdAndActivoOrderByPatenteAsc(empresaId, true);
        };

        return base.stream().map(this::toResp).toList();
    }

    @Transactional
    public VehiculoResponse create(VehiculoRequest req) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        String patente = req.patente().trim().toUpperCase();

        if (repo.existsByEmpresa_IdAndPatenteIgnoreCase(empresaId, patente)) {
            throw new BadRequestException("Ya existe un vehículo con esa patente");
        }

        // Crear Punto Operativo del vehículo
        PuntoOperativo po = new PuntoOperativo();
        po.setEmpresa(u.getEmpresa());
        po.setCodigo(patente);
        po.setNombre("Vehículo " + patente);
        po.setTipo(PuntoOperativoTipo.VEHICULO);
        po.setDireccion("Móvil");
        po.setLocalidad(null);
        po.setLatitud(null);
        po.setLongitud(null);
        po.setActivo(true);
        po = poRepo.save(po);

        Vehiculo v = new Vehiculo();
        v.setEmpresa(u.getEmpresa());
        v.setPatente(patente);
        v.setPuntoOperativo(po);
        v.setActivo(true);

        return toResp(repo.save(v));
    }

    @Transactional
    public VehiculoResponse update(Long id, VehiculoRequest req) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Vehiculo v = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Vehículo no encontrado"));

        String patente = req.patente().trim().toUpperCase();
        if (repo.existsByEmpresa_IdAndPatenteIgnoreCaseAndIdNot(empresaId, patente, id)) {
            throw new BadRequestException("Ya existe un vehículo con esa patente");
        }

        v.setPatente(patente);

        // Mantener sincronizado el PO
        PuntoOperativo po = v.getPuntoOperativo();
        po.setCodigo(patente);
        po.setNombre("Vehículo " + patente);
        poRepo.save(po);

        return toResp(repo.save(v));
    }

    @Transactional
    public void desactivar(Long id) {
        setActivo(id, false);
    }

    @Transactional
    public void activar(Long id) {
        setActivo(id, true);
    }

    private void setActivo(Long id, boolean activo) {
        var u = currentUser.requireUsuario();
        Long empresaId = u.getEmpresa().getId();

        Vehiculo v = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Vehículo no encontrado"));

        v.setActivo(activo);
        repo.save(v);

        PuntoOperativo po = v.getPuntoOperativo();
        po.setActivo(activo);
        poRepo.save(po);
    }

    private VehiculoResponse toResp(Vehiculo v) {
        return new VehiculoResponse(v.getId(), v.getPatente(), v.getPuntoOperativo().getId(), v.isActivo());
    }
}