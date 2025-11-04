package persistencia;

import java.util.List;
import logica.Evento;

public interface IPersistenciaEventos {
    void salvarEventos(String ruta, List<Evento> eventos) throws Exception;
    List<Evento> cargarEventos(String ruta) throws Exception;
}
