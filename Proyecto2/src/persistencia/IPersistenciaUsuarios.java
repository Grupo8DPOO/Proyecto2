package persistencia;

import java.util.List;
import logica.Usuario;

public interface IPersistenciaUsuarios {
    void salvarUsuarios(String ruta, List<Usuario> usuarios) throws Exception;
    List<Usuario> cargarUsuarios(String ruta) throws Exception;
}
