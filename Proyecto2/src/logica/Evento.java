package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nombre;
    private LocalDateTime fechaHora;
    private String tipo;
    private String estado;
    private Organizador organizador;
    private Venue venue;
    private List<Localidad> localidades;

    public Evento(String nombre, LocalDateTime fechaHora, String tipo, Organizador organizador, Venue venue) {
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.estado = "Activo";
        this.organizador = organizador;
        this.venue = venue;
        this.localidades = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    public Venue getVenue() {
        return venue;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void agregarLocalidad(Localidad l) {
        if (l != null && !localidades.contains(l)) {
            localidades.add(l);
        }
    }

    public List<Tiquete> getAllTiquetesVendidos() {
        List<Tiquete> vendidos = new ArrayList<>();
        for (Localidad l : localidades) {
            for (Tiquete t : l.getTiquetes()) {
                if (!t.isDisponible()) {
                    vendidos.add(t);
                }
            }
        }
        return vendidos;
    }
}
