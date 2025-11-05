package log;

import java.io.IOException;
import java.util.List;

public interface IPersistenciaLog {

    void append(LogEntrada entrada) throws IOException;
    List<LogEntrada> leerTodo() throws IOException;
}
