package marketplace;

import java.util.List;
import java.util.stream.Collectors;

import log.AdminLog;
import log.LogEntrada;
import logica.Administrador;
import logica.Cliente;
import logica.Sistema;
import logica.Tiquete;

public class ControlMarketplace {

    private final IPersistenciaMarketplace repo;
    private final AdminLog log;

    public ControlMarketplace(IPersistenciaMarketplace repo, AdminLog log) {
        this.repo = repo;
        this.log = log;
    }


    public Oferta publicarOferta(Cliente vendedor, List<Tiquete> tiqs, double precio) {
        if (precio <= 0) throw new IllegalArgumentException("Precio inválido");
        if (tiqs == null || tiqs.isEmpty()) throw new IllegalArgumentException("No hay tiquetes");

        for (Tiquete t : tiqs) {
  
        }

        List<Integer> ids = tiqs.stream().map(Tiquete::getId).collect(Collectors.toList());
        Oferta o = Oferta.nueva(vendedor.getCorreo(), ids, precio);
        repo.guardarOferta(o);
        log.log(vendedor.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "oferta=" + o.getId() + " tiquetes=" + ids + " precio=" + precio);
        return o;
    }


    public void cancelarOferta(Cliente vendedor, String idOferta) {
        Oferta o = repo.buscarOferta(idOferta);
        if (o == null) throw new IllegalArgumentException("Oferta no existe");
        if (!o.getIdVendedor().equals(vendedor.getCorreo()))
            throw new SecurityException("No puedes cancelar una oferta de otro usuario");
        o.cancelar();
        repo.guardarOferta(o);
        log.log(vendedor.getCorreo(), LogEntrada.Tipo.OFERTA_CANCELADA, "oferta=" + idOferta);
    }

    public void removerOfertaComoAdmin(Administrador admin, String idOferta) {
        Oferta o = repo.buscarOferta(idOferta);
        if (o == null) throw new IllegalArgumentException("Oferta no existe");
        o.removerPorAdmin();
        repo.guardarOferta(o);
        log.log(admin.getCorreo(), LogEntrada.Tipo.OFERTA_REMOVIDA_ADMIN, "oferta=" + idOferta);
    }

    public List<Oferta> listarPublicadas() {
        return repo.listarPublicadas();
    }
    public void comprarOferta(Cliente comprador, String idOferta) {
        Oferta o = repo.buscarOferta(idOferta);
        if (o == null) throw new IllegalArgumentException("La oferta no existe");
        if (o.getEstado() != Oferta.Estado.PUBLICADA)
            throw new IllegalStateException("La oferta no está disponible para compra");
        o.marcarVendidaA(comprador.getCorreo());
        repo.guardarOferta(o);
        log.log(comprador.getCorreo(), LogEntrada.Tipo.COMPRA_DIRECTA,
                "compra directa oferta=" + o.getId() + " vendedor=" + o.getIdVendedor() +
                " precio=" + o.getPrecioPublicado());
        log.log(o.getIdVendedor(), LogEntrada.Tipo.VENTA_COMPLETADA,
                "venta completada oferta=" + o.getId() + " comprador=" + comprador.getCorreo());
    }
    public Contraoferta proponerContraoferta(Cliente comprador, String idOferta, double nuevoPrecio) {
        Oferta o = repo.buscarOferta(idOferta);
        if (o == null) throw new IllegalArgumentException("La oferta no existe");
        if (o.getEstado() != Oferta.Estado.PUBLICADA)
            throw new IllegalStateException("No puedes hacer contraoferta: la oferta no está activa");
        if (nuevoPrecio <= 0 || nuevoPrecio >= o.getPrecioPublicado())
            throw new IllegalArgumentException("El precio propuesto debe ser menor al publicado");

        Contraoferta cf = new Contraoferta(idOferta, comprador.getCorreo(), nuevoPrecio);

        log.log(comprador.getCorreo(), LogEntrada.Tipo.CONTRAOFERTA_PROPUESTA,
                "contraoferta=" + cf.getId() + " oferta=" + idOferta + " nuevoPrecio=" + nuevoPrecio);

        return cf;
    }

    public void aceptarContraoferta(Cliente vendedor, Contraoferta cf) {
        Oferta o = repo.buscarOferta(cf.getIdOfertaOriginal());
        if (o == null) throw new IllegalArgumentException("Oferta original no encontrada");
        if (!o.getIdVendedor().equals(vendedor.getCorreo()))
            throw new SecurityException("Solo el vendedor puede aceptar");
        if (cf.getEstado() != Contraoferta.Estado.PENDIENTE)
            throw new IllegalStateException("La contraoferta ya fue procesada");

        cf.aceptar();
        o.marcarVendidaA(cf.getIdComprador());
        repo.guardarOferta(o);

        log.log(vendedor.getCorreo(), LogEntrada.Tipo.CONTRAOFERTA_ACEPTADA,
                "oferta=" + o.getId() + " contraoferta=" + cf.getId() + " precio=" + cf.getPrecioPropuesto());
        log.log(vendedor.getCorreo(), LogEntrada.Tipo.VENTA_COMPLETADA,
                "venta completada por contraoferta a " + cf.getIdComprador());
    }

    public void rechazarContraoferta(Cliente vendedor, Contraoferta cf) {
        Oferta o = repo.buscarOferta(cf.getIdOfertaOriginal());
        if (o == null) throw new IllegalArgumentException("Oferta original no encontrada");
        if (!o.getIdVendedor().equals(vendedor.getCorreo()))
            throw new SecurityException("Solo el vendedor puede rechazar");
        if (cf.getEstado() != Contraoferta.Estado.PENDIENTE)
            throw new IllegalStateException("La contraoferta ya fue procesada");

        cf.rechazar();
        log.log(vendedor.getCorreo(), LogEntrada.Tipo.CONTRAOFERTA_RECHAZADA,
                "oferta=" + o.getId() + " contraoferta=" + cf.getId() + " comprador=" + cf.getIdComprador());
    }
    public Oferta buscarOferta(String idOferta) {
        var o = repo.buscarOferta(idOferta);
        if (o == null) throw new IllegalArgumentException("La oferta no existe");
        return o;
    }

}
