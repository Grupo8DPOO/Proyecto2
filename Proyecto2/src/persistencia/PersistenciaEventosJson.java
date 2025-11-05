package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.Evento;
import logica.Localidad;
import logica.Organizador;
import logica.Venue;

public class PersistenciaEventosJson implements IPersistenciaEventos {

    private static final String EVENTOS = "eventos";

    @Override
    public void salvarEventos(String archivo, List<Evento> eventos) throws IOException {
        JSONObject raiz = new JSONObject();
        JSONArray jEventos = new JSONArray();

        for (Evento e : eventos) {
            JSONObject jEvento = new JSONObject();
            jEvento.put("nombre", e.getNombre());
            jEvento.put("fechaHora", e.getFechaHora().toString());
            jEvento.put("tipo", e.getTipo());
            jEvento.put("estado", e.getEstado());
            jEvento.put("organizador", e.getOrganizador().getCorreo());
            jEvento.put("venue", e.getVenue().getNombre());

            JSONArray jLocalidades = new JSONArray();
            for (Localidad l : e.getLocalidades()) {
                JSONObject jLocalidad = new JSONObject();
                jLocalidad.put("nombre", l.getNombre());
                jLocalidad.put("numerada", l.isNumerada());
                jLocalidad.put("precioBase", l.getPrecioBase());
                jLocalidad.put("capacidad", l.getCapacidad());
                jLocalidades.put(jLocalidad);
            }

            jEvento.put("localidades", jLocalidades);
            jEventos.put(jEvento);
        }

        raiz.put(EVENTOS, jEventos);
        try (PrintWriter pw = new PrintWriter(archivo)) {
            raiz.write(pw, 2, 0);
        }
    }

    @Override
    public List<Evento> cargarEventos(String archivo) throws IOException {
        List<Evento> eventos = new java.util.ArrayList<>();
        String contenido = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray jEventos = raiz.getJSONArray(EVENTOS);

        for (int i = 0; i < jEventos.length(); i++) {
            JSONObject jEvento = jEventos.getJSONObject(i);
            String nombre = jEvento.getString("nombre");
            LocalDateTime fecha = LocalDateTime.parse(jEvento.getString("fechaHora"));
            String tipo = jEvento.getString("tipo");
            String estado = jEvento.getString("estado");

            Organizador org = new Organizador(jEvento.getString("organizador"), "pass", jEvento.getString("organizador"));
            Venue v = new Venue(jEvento.getString("venue"), "ubicación", 0);

            Evento e = new Evento(nombre, fecha, tipo, org, v);
            e.setEstado(estado);

            JSONArray jLocalidades = jEvento.getJSONArray("localidades");
            for (int j = 0; j < jLocalidades.length(); j++) {
                JSONObject jLoc = jLocalidades.getJSONObject(j);
                String nombreLoc = jLoc.getString("nombre");
                boolean numerada = jLoc.getBoolean("numerada");
                double precioBase = jLoc.getDouble("precioBase");
                int capacidad = jLoc.getInt("capacidad");
                e.agregarLocalidad(new Localidad(nombreLoc, numerada, precioBase, capacidad, e));
            }

            eventos.add(e);
        }
        return eventos;
    }
}

