package ar.com.encogas.service.catalog;

import ar.com.encogas.domain.catalog.Cliente;
import ar.com.encogas.dto.catalog.ClienteRequest;
import ar.com.encogas.dto.catalog.ClienteResponse;
import ar.com.encogas.exception.BadRequestException;
import ar.com.encogas.exception.NotFoundException;
import ar.com.encogas.repository.catalog.ClienteRepository;
import ar.com.encogas.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    public enum Estado { ACTIVOS, INACTIVOS, TODOS }

    private final ClienteRepository repo;
    private final CurrentUserService currentUserService;

    public ClienteService(ClienteRepository repo, CurrentUserService currentUserService) {
        this.repo = repo;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> list(Estado estado, String search) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        if (estado == null) estado = Estado.ACTIVOS;

        List<Cliente> base = switch (estado) {
            case TODOS -> repo.findByEmpresa_IdOrderByRazonSocialAsc(empresaId);
            case INACTIVOS -> repo.findByEmpresa_IdAndActivoOrderByRazonSocialAsc(empresaId, false);
            default -> repo.findByEmpresa_IdAndActivoOrderByRazonSocialAsc(empresaId, true);
        };

        String s = (search == null) ? "" : search.trim().toLowerCase();
        if (!s.isEmpty()) {
            base = base.stream()
                    .filter(c ->
                            contains(c.getRazonSocial(), s) ||
                                    contains(c.getCuitDni(), s) ||
                                    contains(c.getDireccion(), s) ||
                                    contains(c.getLocalidad(), s) ||
                                    contains(c.getTelefono(), s) ||
                                    contains(c.getEmail(), s)
                    )
                    .toList();
        }

        return base.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse get(Long id) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        Cliente c = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return toResponse(c);
    }

    @Transactional
    public ClienteResponse create(ClienteRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        validateCoords(req);

        String cuit = normalizeCuit(req.cuitDni());
        if (cuit != null && repo.existsByEmpresa_IdAndCuitDniIgnoreCase(empresaId, cuit)) {
            throw new BadRequestException("Ya existe un cliente con ese CUIT/DNI");
        }

        Cliente c = new Cliente();
        c.setEmpresa(usuario.getEmpresa());
        c.setRazonSocial(req.razonSocial().trim());
        c.setCuitDni(cuit);
        c.setDireccion(req.direccion().trim());
        c.setLocalidad(trimOrNull(req.localidad()));
        c.setTelefono(trimOrNull(req.telefono()));
        c.setEmail(trimOrNull(req.email()));
        c.setLatitud(req.latitud());
        c.setLongitud(req.longitud());
        c.setActivo(true);

        return toResponse(repo.save(c));
    }

    @Transactional
    public ClienteResponse update(Long id, ClienteRequest req) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        Cliente c = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        validateCoords(req);

        String cuit = normalizeCuit(req.cuitDni());
        if (cuit != null && repo.existsByEmpresa_IdAndCuitDniIgnoreCaseAndIdNot(empresaId, cuit, id)) {
            throw new BadRequestException("Ya existe un cliente con ese CUIT/DNI");
        }

        c.setRazonSocial(req.razonSocial().trim());
        c.setCuitDni(cuit);
        c.setDireccion(req.direccion().trim());
        c.setLocalidad(trimOrNull(req.localidad()));
        c.setTelefono(trimOrNull(req.telefono()));
        c.setEmail(trimOrNull(req.email()));
        c.setLatitud(req.latitud());
        c.setLongitud(req.longitud());

        return toResponse(repo.save(c));
    }

    @Transactional
    public void activar(Long id) { setActivo(id, true); }

    @Transactional
    public void desactivar(Long id) { setActivo(id, false); }

    private void setActivo(Long id, boolean activo) {
        var usuario = currentUserService.requireUsuario();
        Long empresaId = usuario.getEmpresa().getId();

        Cliente c = repo.findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        c.setActivo(activo);
        repo.save(c);
    }

    private void validateCoords(ClienteRequest req) {
        boolean hasLat = req.latitud() != null;
        boolean hasLon = req.longitud() != null;
        if (hasLat != hasLon) {
            throw new BadRequestException("Latitud y longitud deben cargarse juntas");
        }
    }

    private String normalizeCuit(String raw) {
        if (raw == null) return null;
        String v = raw.trim();
        if (v.isEmpty()) return null;
        return v;
    }

    private String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase().contains(needle);
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getRazonSocial(),
                c.getCuitDni(),
                c.getDireccion(),
                c.getLocalidad(),
                c.getTelefono(),
                c.getEmail(),
                c.getLatitud(),
                c.getLongitud(),
                c.isActivo()
        );
    }
}