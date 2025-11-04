package logica;

import java.time.LocalDateTime;

public class Venue {
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private boolean aprobado;

    public Venue(String nombre, String ubicacion, int capacidadMaxima) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.aprobado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean estaAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public boolean estaDisponible(LocalDateTime fecha) {
        return true;
    }
}
