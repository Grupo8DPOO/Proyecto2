package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.Venue;

public class PersistenciaVenuesJson implements IPersistenciaVenues {

    private static final String VENUES = "venues";

    @Override
    public void salvarVenues(String archivo, List<Venue> venues) throws IOException {
        JSONObject raiz = new JSONObject();
        JSONArray jVenues = new JSONArray();

        for (Venue v : venues) {
            JSONObject jVenue = new JSONObject();
            jVenue.put("nombre", v.getNombre());
            jVenue.put("ubicacion", v.getUbicacion());
            jVenue.put("capacidadMaxima", v.getCapacidadMaxima());
            jVenue.put("aprobado", v.estaAprobado());
            jVenues.put(jVenue);
        }

        raiz.put(VENUES, jVenues);
        try (PrintWriter pw = new PrintWriter(archivo)) {
            raiz.write(pw, 2, 0);
        }
    }

    @Override
    public List<Venue> cargarVenues(String archivo) throws IOException {
        List<Venue> venues = new ArrayList<>();

        String contenido = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray jVenues = raiz.getJSONArray(VENUES);

        for (int i = 0; i < jVenues.length(); i++) {
            JSONObject jVenue = jVenues.getJSONObject(i);
            String nombre = jVenue.getString("nombre");
            String ubicacion = jVenue.getString("ubicacion");
            int capacidad = jVenue.getInt("capacidadMaxima");
            boolean aprobado = jVenue.getBoolean("aprobado");

            Venue v = new Venue(nombre, ubicacion, capacidad);
            v.setAprobado(aprobado);
            venues.add(v);
        }

        return venues;
    }
}