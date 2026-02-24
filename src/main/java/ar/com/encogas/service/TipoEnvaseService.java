package ar.com.encogas.service.catalog;

import ar.com.encogas.domain.catalog.TipoEnvase;
import ar.com.encogas.dto.catalog.TipoEnvaseRequest;
import ar.com.encogas.dto.catalog.TipoEnvaseResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.TipoEnvaseRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoEnvaseService {

    public enum Estado { ACTIVOS, INACTIVOS, TODOS }

    private final TipoEnvaseRepository repo;
    private final CurrentUserService currentUserService;

    public TipoEnvaseService(TipoEnvaseRepository repo, CurrentUserService currentUserService) {
        this.repo = repo;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<TipoEnvaseResponse> list(Estado estado, String search) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        List<TipoEnvase> base;
        if (estado == null) estado = Estado.ACTIVOS;

        switch (estado) {
            case TODOS -> base = repo.findByEmpresa_IdOrderByNombreAscCapacidadKgAsc(empresaId);
            case INACTIVOS -> base = repo.findByEmpresa_IdAndActivoOrderByNombreAscCapacidadKgAsc(empresaId, false);
            default -> base = repo.findByEmpresa_IdAndActivoOrderByNombreAscCapacidadKgAsc(empresaId, true);
        }

        String s = (search == null) ? "" : search.trim().toLowerCase();
        if (!s.isEmpty()) {
            base = base.stream()
                    .filter(t ->
                            (t.getNombre() != null && t.getNombre().toLowerCase().contains(s)) ||
                                    (t.getCodigo() != null && t.getCodigo().toLowerCase().contains(s)))
                    .toList();
        }

        return base.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TipoEnvaseResponse get(Long id) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        TipoEnvase te = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));
        return toResponse(te);
    }

    @Transactional
    public TipoEnvaseResponse create(TipoEnvaseRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        validateUniqueForCreate(empresaId, req);

        TipoEnvase te = new TipoEnvase();
        te.setEmpresa(usuario.getEmpresa());
        te.setCodigo(req.codigo().trim());
        te.setNombre(req.nombre().trim());
        te.setCapacidadKg(req.capacidadKg());
        te.setActivo(true);

        return toResponse(repo.save(te));
    }

    @Transactional
    public TipoEnvaseResponse update(Long id, TipoEnvaseRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        TipoEnvase te = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));

        validateUniqueForUpdate(empresaId, id, req);

        te.setCodigo(req.codigo().trim());
        te.setNombre(req.nombre().trim());
        te.setCapacidadKg(req.capacidadKg());

        return toResponse(repo.save(te));
    }

    @Transactional
    public void activar(Long id) {
        setActivo(id, true);
    }

    @Transactional
    public void desactivar(Long id) {
        setActivo(id, false);
    }

    private void setActivo(Long id, boolean activo) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        TipoEnvase te = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));

        te.setActivo(activo);
        repo.save(te);
    }

    private void validateUniqueForCreate(Long empresaId, TipoEnvaseRequest req) {
        if (repo.existsByEmpresa_IdAndCodigoIgnoreCase(empresaId, req.codigo().trim())) {
            throw new BadRequestException("Ya existe un tipo de envase con ese código");
        }
        if (repo.existsByEmpresa_IdAndNombreIgnoreCaseAndCapacidadKg(empresaId, req.nombre().trim(), req.capacidadKg())) {
            throw new BadRequestException("Ya existe un tipo de envase con ese nombre y capacidad");
        }
    }

    private void validateUniqueForUpdate(Long empresaId, Long id, TipoEnvaseRequest req) {
        if (repo.existsByEmpresa_IdAndCodigoIgnoreCaseAndIdNot(empresaId, req.codigo().trim(), id)) {
            throw new BadRequestException("Ya existe un tipo de envase con ese código");
        }
        if (repo.existsByEmpresa_IdAndNombreIgnoreCaseAndCapacidadKgAndIdNot(empresaId, req.nombre().trim(), req.capacidadKg(), id)) {
            throw new BadRequestException("Ya existe un tipo de envase con ese nombre y capacidad");
        }
    }

    private TipoEnvaseResponse toResponse(TipoEnvase te) {
        return new TipoEnvaseResponse(te.getId(), te.getCodigo(), te.getNombre(), te.getCapacidadKg(), te.isActivo());
    }
}