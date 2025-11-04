package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Organizador extends Cliente {
    private String nombre;
    private List<Evento> eventosCreados;
    private double ganancias;

    public Organizador(String correo, String contrasena, String nombre) {
        super(correo, contrasena);
        this.nombre = nombre;
        this.eventosCreados = new ArrayList<>();
        this.ganancias = 0.0;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Evento> getEventosCreados() {
        return eventosCreados;
    }

    public Evento crearEvento(String nombreEvento, LocalDateTime fechaHora, String tipo, Venue venue) {
        try {
            if (venue == null) {
            	throw new Exception("Venue inválido");
            }
            if (!venue.estaAprobado()) {
            	throw new Exception("Venue no aprobado");
            }
            if (!venue.estaDisponible(fechaHora)) {
            	throw new Exception("Venue ocupado en esa fecha");
            }
            Evento e = new Evento(nombreEvento, fechaHora, tipo, this, venue);
            eventosCreados.add(e);
            return e;
        } 
        catch (Exception e) {
            System.out.println("Error al crear evento");
            e.printStackTrace();
            return null;
        }
    }

    public Localidad definirLocalidad(String nombreLocalidad, boolean numerada, double precioBase, int capacidad, Evento evento) {
        try {
            if (evento == null) {
            	throw new Exception("Evento no válido");
            }
            Localidad l = new Localidad(nombreLocalidad, numerada, precioBase, capacidad, evento);
            evento.agregarLocalidad(l);
            return l;
        } 
        catch (Exception e) {
            System.out.println("Error al definir localidad");
            e.printStackTrace();
            return null;
        }
    }

    public void establecerPrecio(Localidad l, double nuevoPrecio) {
        try {
            if (l == null) {
            	throw new Exception("Localidad no válida");
            }
            l.setPrecioBase(nuevoPrecio);
        } 
        catch (Exception e) {
            System.out.println("Error al establecer precio");
            e.printStackTrace();
        }
    }

    public double consultarGanancias() {
        return ganancias;
    }

    public void registrarGanancia(double cantidad) {
        ganancias += cantidad;
    }
}
