package presentacion;

import java.time.LocalDateTime;
import logica.Administrador;
import logica.Cliente;
import logica.Evento;
import logica.Localidad;
import logica.Organizador;
import logica.Sistema;
import logica.Tiquete;
import logica.Venue;
import log.LogEntrada;

public class Principal {

    private Sistema sistema;

    public Principal() {
        sistema = new Sistema();
        caso1();
        caso2();
    }

    private void caso1() {
        System.out.println("\n=== CASO 1 ===");

        Administrador admin = new Administrador("martin@dpoo.com", "1234");
        Sistema.setAdministrador(admin);

        sistema.getAudit().log(admin.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Administrador creado y asignado: " + admin.getCorreo());

        Venue v = new Venue("Auditorio ML", "Carrera 1e", 500);
        Sistema.registrarVenue(v);
        admin.aprobarVenue(v);
        sistema.getAudit().log(admin.getCorreo(), LogEntrada.Tipo.VENTA_COMPLETADA,
                "Administrador aprobó venue: " + v.getNombre());

        Organizador org = new Organizador("isabella@dpoo.com", "abcd", "Organizador 1");
        Sistema.registrarUsuario(org);

        LocalDateTime fecha = LocalDateTime.now().plusDays(5);
        Evento e = org.crearEvento("Musical Shakespeare", fecha, "musical", v);
        Sistema.registrarEvento(e);
        sistema.getAudit().log(org.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Organizador creó evento: " + e.getNombre());

        Localidad loc = org.definirLocalidad("General", false, 100.0, 50, e);
        loc.generarNTiquetes(5);
        sistema.getAudit().log(org.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Localidad definida: " + loc.getNombre() + " con " + loc.getTiquetes().size() + " tiquetes");

        Cliente c1 = new Cliente("juanca@dpoo.com", "5678");
        c1.setSaldo(500.0);
        Sistema.registrarUsuario(c1);

        Cliente c2 = new Cliente("hector@dpoo.com", "efgh");
        c2.setSaldo(300.0);
        Sistema.registrarUsuario(c2);

        Tiquete t1 = loc.getTiquetes().get(0);
        c1.comprarTiquete(t1);
        sistema.getAudit().log(c1.getCorreo(), LogEntrada.Tipo.COMPRA_DIRECTA,
                "Cliente " + c1.getCorreo() + " compró tiquete " + t1.getId());

        c1.transferirTiquete(t1, c2, "pass1");
        sistema.getAudit().log(c1.getCorreo(), LogEntrada.Tipo.VENTA_COMPLETADA,
                "Cliente " + c1.getCorreo() + " transfirió tiquete " + t1.getId() + " a " + c2.getCorreo());

        admin.setCargoFijo(2.0);
        admin.fijarCargoPorTipo("musical", 10.0);
        sistema.getAudit().log(admin.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Administrador fijó cargos para tipo musical.");

        admin.cancelarEvento(e, true);
        sistema.getAudit().log(admin.getCorreo(), LogEntrada.Tipo.OFERTA_CANCELADA,
                "Administrador canceló evento: " + e.getNombre());

        System.out.println("Evento cancelado: " + e.getNombre());
        System.out.println("Estado del evento: " + e.getEstado());
        System.out.println("Saldo cliente 1: " + c1.getSaldo());
        System.out.println("Saldo cliente 2: " + c2.getSaldo());
    }

    private void caso2() {
        System.out.println("\n=== CASO 2 ===");

        Administrador admin = new Administrador("pepito@dpoo.com", "ijkl");
        Sistema.setAdministrador(admin);
        sistema.getAudit().log(admin.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Administrador creado: " + admin.getCorreo());

        admin.aprobarVenue(null);

        Venue v = new Venue("Auditorio Lleras", "Calle 19A", 1000);
        Sistema.registrarVenue(v);

        Organizador org = new Organizador("perensejo@dpoo.com", "0910", "Organizador 2");
        Sistema.registrarUsuario(org);

        Evento e = org.crearEvento("Concierto fallido", LocalDateTime.now().plusDays(1), "musical", v);
        sistema.getAudit().log(org.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Intento de crear evento fallido en venue no aprobado.");

        admin.aprobarVenue(v);
        e = org.crearEvento("Concierto correcto", LocalDateTime.now().plusDays(1), "musical", v);
        Sistema.registrarEvento(e);
        sistema.getAudit().log(org.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Evento aprobado: " + e.getNombre());

        Localidad l = org.definirLocalidad("VIP", true, 200.0, 5, e);
        l.generarNTiquetes(3);

        Cliente c = new Cliente("batman@dpoo.com", "mnop");
        Sistema.registrarUsuario(c);
        c.setSaldo(50);
        sistema.getAudit().log(c.getCorreo(), LogEntrada.Tipo.OFERTA_PUBLICADA,
                "Cliente creado con saldo inicial 50");

        Tiquete t = l.getTiquetes().get(0);
        c.comprarTiquete(t);
        sistema.getAudit().log(c.getCorreo(), LogEntrada.Tipo.COMPRA_DIRECTA,
                "Cliente " + c.getCorreo() + " intentó comprar con saldo insuficiente.");

        c.setSaldo(500);
        c.comprarTiquete(t);
        sistema.getAudit().log(c.getCorreo(), LogEntrada.Tipo.COMPRA_DIRECTA,
                "Cliente " + c.getCorreo() + " compró tiquete " + t.getId());

        Cliente c2 = new Cliente("robin@dpoo.com", "1112");
        Sistema.registrarUsuario(c2);

        c.transferirTiquete(t, c2, "incorrecta");
        sistema.getAudit().log(c.getCorreo(), LogEntrada.Tipo.CONTRAOFERTA_RECHAZADA,
                "Cliente " + c.getCorreo() + " intentó transferir con clave incorrecta.");

        c.transferirTiquete(t, c2, "1112");
        sistema.getAudit().log(c.getCorreo(), LogEntrada.Tipo.VENTA_COMPLETADA,
                "Cliente " + c.getCorreo() + " transfirió tiquete a " + c2.getCorreo());
    }

   
    public static void main(String[] args) {
        new Principal();
    }
}


