package logica;

import java.util.HashMap;
import java.util.Map;

public class Administrador extends Usuario {
    private static double cargoFijo = 1.0;
    private Map<String, Double> porcentajesPorTipo;

    public Administrador(String correo, String contrasena) {
        super(correo, contrasena);
        this.porcentajesPorTipo = new HashMap<>();
    }

    public boolean aprobarVenue(Venue v) {
        try {
            if (v == null) {
            	throw new Exception("Venue inválido");
            }
            v.setAprobado(true);
            return true;
        } 
        catch (Exception e) {
            System.out.println("Error al aprobar venue");
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelarEvento(Evento e, boolean porAdministrador) {
        try {
            if (e == null) {
            	throw new Exception("Evento inválido");
            }
            e.setEstado("Cancelado");
            if (porAdministrador) {
                for (Tiquete t : e.getAllTiquetesVendidos()) {
                    Cliente dueño = t.getDueno();
                    if (dueño != null) {
                        double reembolso = t.getPrecio();
                        dueño.setSaldo(dueño.getSaldo() + reembolso - cargoFijo);
                    }
                }
            }
            return true;
        } 
        catch (Exception ex) {
            System.out.println("Error al cancelar evento");
            ex.printStackTrace();
            return false;
        }
    }

    public void fijarCargoPorTipo(String tipo, double porcentaje) {
        porcentajesPorTipo.put(tipo, porcentaje);
    }

    public Double getCargoPorTipo(String tipo) {
        return porcentajesPorTipo.getOrDefault(tipo, 0.0);
    }

    public void setCargoFijo(double cargo) {
    	cargoFijo = cargo;
    }

    public static double getCargoFijo() {
        return cargoFijo;
    }
}
