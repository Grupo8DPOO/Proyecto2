package persistencia;

import java.util.List;
import logica.Venue;

public interface IPersistenciaVenues {
    void salvarVenues(String archivo, List<Venue> venues) throws Exception;
    List<Venue> cargarVenues(String archivo) throws Exception;
}

