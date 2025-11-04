package logica;

import java.util.ArrayList;
import java.util.List;

public class Localidad {
    private String nombre;
    private boolean numerada;
    private double precioBase;
    private int capacidad;
    private Evento evento;
    private List<Tiquete> tiquetes;

    public Localidad(String nombre, boolean numerada, double precioBase, int capacidad, Evento evento) {
        this.nombre = nombre;
        this.numerada = numerada;
        this.precioBase = precioBase;
        this.capacidad = capacidad;
        this.evento = evento;
        this.tiquetes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isNumerada() {
        return numerada;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public Evento getEvento() {
        return evento;
    }

    public List<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public void generarNTiquetes(int n) {
        try {
            if (n <= 0) {
            	throw new Exception("Cantidad inválida de tiquetes");
            }
            if (tiquetes.size() + n > capacidad) {
            	throw new Exception("Capacidad excedida");
            }
            for (int i = 0; i < n; i++) {
                Tiquete t;
                if (numerada) {
                    t = new TiqueteNumerado(this, precioBase, i + 1);
                } 
                else {
                    t = new TiqueteSimple(this, precioBase);
                }
                tiquetes.add(t);
            }
        } 
        catch (Exception e) {
            System.out.println("Error al generar tiquetes: " + e.getMessage());
        }
    }

    public int capacidadRestante() {
        return capacidad - tiquetes.size();
    }
}
