package log;

import java.io.IOException;
import java.util.List;

import logica.Administrador;
import logica.Usuario;


public class AdminLog {

    private final IPersistenciaLog repo;

    public AdminLog(IPersistenciaLog repo) {
        this.repo = repo;
    }

    public void log(String actorId, LogEntrada.Tipo tipo, String detalle) {
        try {
            repo.append(new LogEntrada(actorId, tipo, detalle));
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo el log", e);
        }
    }

    public List<LogEntrada> listar(Usuario solicitante) {
        if (!(solicitante instanceof Administrador)) {
            throw new SecurityException("Solo el administrador puede consultar el log.");
        }
        try {
            return repo.leerTodo();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el log", e);
        }
    }
}

