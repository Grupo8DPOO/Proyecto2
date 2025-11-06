package logica;

import java.util.ArrayList;
import java.util.List;

import log.AdminLog;
import log.PersistenciaLogJson;
import persistencia.CentralPersistencia;

public class Sistema {

    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Evento> eventos = new ArrayList<>();
    private static List<Venue> venues = new ArrayList<>();
    private static Administrador administrador;

    private final AdminLog log;

    public Sistema() {
        this.log = new AdminLog(new PersistenciaLogJson("data/log.jsonl"));
    }


    public AdminLog getLog() {
        return log;
    }

    public static void setAdministrador(Administrador admin) {
        administrador = admin;
    }

    public static Administrador getAdministrador() {
        return administrador;
    }

    public static void registrarUsuario(Usuario u) {
        if (u != null && !usuarios.contains(u)) {
            usuarios.add(u);
        }
    }

    public static void registrarEvento(Evento e) {
        if (e != null && !eventos.contains(e)) {
            eventos.add(e);
        }
    }

    public static void registrarVenue(Venue v) {
        if (v != null && !venues.contains(v)) {
            venues.add(v);
        }
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static List<Evento> getEventos() {
        return eventos;
    }

    public static List<Venue> getVenues() {
        return venues;
    }
    
}

