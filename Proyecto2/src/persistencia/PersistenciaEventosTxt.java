package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import logica.Evento;
import logica.Organizador;
import logica.Venue;

public class PersistenciaEventosTxt implements IPersistenciaEventos {

    @Override
    public void salvarEventos(String ruta, List<Evento> eventos) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Evento e : eventos) {
                bw.write(e.getNombre() + ";" 
                        + e.getTipo() + ";" 
                        + e.getFechaHora() + ";" 
                        + e.getEstado() + ";" 
                        + (e.getOrganizador() != null ? e.getOrganizador().getCorreo() : "null") + ";" 
                        + (e.getVenue() != null ? e.getVenue().getNombre() : "null"));
                bw.newLine();
            }
        } 
        catch (Exception e) {
            throw new Exception("Error al guardar eventos");
        }
    }

    @Override
    public List<Evento> cargarEventos(String ruta) throws Exception {
        List<Evento> eventos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String nombre = partes[0];
                String tipo = partes[1];
                LocalDateTime fecha = LocalDateTime.parse(partes[2]);
                String estado = partes[3];
                Organizador org = new Organizador(partes[4], "pass", partes[4]);
                Venue v = new Venue(partes[5], "ubicación", 0);
                Evento e = new Evento(nombre, fecha, tipo, org, v);
                e.setEstado(estado);
                eventos.add(e);
            }
        } 
        catch (Exception e) {
            throw new Exception("Error al cargar eventos");
        }
        return eventos;
    }
}
