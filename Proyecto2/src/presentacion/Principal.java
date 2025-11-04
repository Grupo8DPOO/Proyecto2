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

public class Principal {

    public Principal() {
        caso1();
        caso2();
    }

    private void caso1() {
        System.out.println("\n=== CASO 1 ===");

        Administrador admin = new Administrador("martin@dpoo.com", "1234");
        Sistema.setAdministrador(admin);

        Venue v = new Venue("Auditorio ML", "Carrera 1e", 500);
        Sistema.registrarVenue(v);
        admin.aprobarVenue(v);

        Organizador org = new Organizador("isabella@dpoo.com", "abcd", "Organizador 1");
        Sistema.registrarUsuario(org);

        LocalDateTime fecha = LocalDateTime.now().plusDays(5);
        Evento e = org.crearEvento("Musical Shakespeare", fecha, "musical", v);
        Sistema.registrarEvento(e);

        Localidad loc = org.definirLocalidad("General", false, 100.0, 50, e);
        loc.generarNTiquetes(5);

        Cliente c1 = new Cliente("juanca@dpoo.com", "5678");
        c1.setSaldo(500.0);
        Sistema.registrarUsuario(c1);

        Cliente c2 = new Cliente("hector@dpoo.com", "efgh");
        c2.setSaldo(300.0);
        Sistema.registrarUsuario(c2);

        Tiquete t1 = loc.getTiquetes().get(0);
        c1.comprarTiquete(t1);

        c1.transferirTiquete(t1, c2, "pass1");

        admin.setCargoFijo(2.0);
        admin.fijarCargoPorTipo("musical", 10.0);

        admin.cancelarEvento(e, true);

        System.out.println("Evento cancelado: " + e.getNombre());
        System.out.println("Estado del evento: " + e.getEstado());
        System.out.println("Saldo cliente 1: " + c1.getSaldo());
        System.out.println("Saldo cliente 2: " + c2.getSaldo());
    }


    private void caso2() {
        System.out.println("\n=== CASO 2 ===");

        Administrador admin = new Administrador("pepito@dpoo.com", "ijkl");
        Sistema.setAdministrador(admin);

        admin.aprobarVenue(null);

        Venue v = new Venue("Auditorio Lleras", "Calle 19A", 1000);
        Sistema.registrarVenue(v);

        Organizador org = new Organizador("perensejo@dpoo.com", "0910", "Organizador 2");
        Sistema.registrarUsuario(org);

        Evento e = org.crearEvento("Concierto fallido", LocalDateTime.now().plusDays(1), "musical", v);

        admin.aprobarVenue(v);
        e = org.crearEvento("Concierto correcto", LocalDateTime.now().plusDays(1), "musical", v);
        Sistema.registrarEvento(e);

        Localidad l = org.definirLocalidad("VIP", true, 200.0, 5, e);
        l.generarNTiquetes(3);

        Cliente c = new Cliente("batman@dpoo.com", "mnop");
        Sistema.registrarUsuario(c);
        c.setSaldo(50);

        Tiquete t = l.getTiquetes().get(0);
        c.comprarTiquete(t); 

        c.setSaldo(500);
        c.comprarTiquete(t); 

        Cliente c2 = new Cliente("robin@dpoo.com", "1112");
        Sistema.registrarUsuario(c2);
        c.transferirTiquete(t, c2, "incorrecta"); 

        c.transferirTiquete(t, c2, "1112"); 
        }

    public static void main(String[] args) {
        new Principal();
    }
}
