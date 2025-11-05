package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class PersistenciaLogJson implements IPersistenciaLog {

    private final Path path;

    public PersistenciaLogJson(String filePath) {
        this.path = Paths.get(filePath);
    }

    @Override
    public void append(LogEntrada entrada) throws IOException {
        Files.createDirectories(path.getParent());
        JSONObject jo = new JSONObject()
                .put("id", entrada.getId())
                .put("actorId", entrada.getActorId())
                .put("tipo", entrada.getTipo().name())
                .put("detalle", entrada.getDetalle())
                .put("tiempo", entrada.getTiempo());

        try (BufferedWriter bw = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            bw.write(jo.toString());
            bw.newLine();
        }
    }

    @Override
    public List<LogEntrada> leerTodo() throws IOException {
        List<LogEntrada> out = new ArrayList<>();
        if (!Files.exists(path)) return out;

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                JSONObject jo = new JSONObject(line);

                String id       = jo.optString("id");
                String actorId  = jo.optString("actorId");
                String tipoStr  = jo.optString("tipo");
                String detalle  = jo.optString("detalle");
                String tiempo   = jo.optString("tiempo");

                LogEntrada.Tipo tipo;
                try { tipo = LogEntrada.Tipo.valueOf(tipoStr); }
                catch (Exception e) { continue; } 


                out.add(new LogEntrada(id, actorId, tipo, detalle, tiempo));
            }
        }
        return out;
    }
}

