package ar.com.encogas.service.catalog;

import ar.com.encogas.domain.catalog.PuntoOperativo;
import ar.com.encogas.domain.catalog.PuntoOperativoTipo;
import ar.com.encogas.dto.catalog.PuntoOperativoRequest;
import ar.com.encogas.dto.catalog.PuntoOperativoResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.PuntoOperativoRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PuntoOperativoService {

    public enum Estado { ACTIVOS, INACTIVOS, TODOS }

    private final PuntoOperativoRepository repo;
    private final CurrentUserService currentUserService;

    public PuntoOperativoService(PuntoOperativoRepository repo, CurrentUserService currentUserService) {
        this.repo = repo;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<PuntoOperativoResponse> list(Estado estado, PuntoOperativoTipo tipo, String search) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        List<PuntoOperativo> base = (estado == Estado.TODOS || estado == null)
                ? repo.findByEmpresa_IdOrderByNombreAsc(empresaId)
                : repo.findByEmpresa_IdAndActivoOrderByNombreAsc(empresaId, estado == Estado.ACTIVOS);

        return base.stream()
                .filter(p -> tipo == null || p.getTipo() == tipo)
                .filter(p -> search == null || search.isBlank() ||
                        p.getNombre().toLowerCase().contains(search.toLowerCase()) ||
                        p.getCodigo().toLowerCase().contains(search.toLowerCase()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PuntoOperativoResponse create(PuntoOperativoRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        validateCasaCentral(empresaId, req.tipo(), null);

        if (repo.existsByEmpresa_IdAndCodigoIgnoreCase(empresaId, req.codigo().trim()))
            throw new BadRequestException("Código ya existente");

        PuntoOperativo p = new PuntoOperativo();
        p.setEmpresa(usuario.getEmpresa());
        apply(req, p);

        return toResponse(repo.save(p));
    }

    @Transactional
    public PuntoOperativoResponse update(Long id, PuntoOperativoRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        PuntoOperativo p = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("No encontrado"));

        validateCasaCentral(empresaId, req.tipo(), id);

        if (repo.existsByEmpresa_IdAndCodigoIgnoreCaseAndIdNot(empresaId, req.codigo().trim(), id))
            throw new BadRequestException("Código ya existente");

        apply(req, p);
        return toResponse(repo.save(p));
    }

    private void validateCasaCentral(Long empresaId, PuntoOperativoTipo tipo, Long excludeId) {
        if (tipo == PuntoOperativoTipo.CASA_CENTRAL) {
            boolean existe = repo.existsByEmpresa_IdAndTipoAndActivoTrue(empresaId, PuntoOperativoTipo.CASA_CENTRAL);
            if (existe && excludeId == null)
                throw new BadRequestException("Ya existe una Casa Central activa");
        }
    }

    private void apply(PuntoOperativoRequest req, PuntoOperativo p) {
        p.setCodigo(req.codigo().trim());
        p.setNombre(req.nombre().trim());
        p.setTipo(req.tipo());
        p.setDireccion(req.direccion().trim());
        p.setLocalidad(req.localidad());
        p.setLatitud(req.latitud());
        p.setLongitud(req.longitud());
    }

    private PuntoOperativoResponse toResponse(PuntoOperativo p) {
        return new PuntoOperativoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getTipo(),
                p.getDireccion(),
                p.getLocalidad(),
                p.getLatitud(),
                p.getLongitud(),
                p.isActivo()
        );
    }
}