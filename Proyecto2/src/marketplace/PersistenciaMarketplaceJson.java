package marketplace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class PersistenciaMarketplaceJson implements IPersistenciaMarketplace {

    private final Path path; 

    public PersistenciaMarketplaceJson(String filePath) {
        this.path = Paths.get(filePath);
    }

    private JSONArray leerArchivo() throws IOException {
        if (!Files.exists(path)) return new JSONArray();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            String contenido = sb.toString().trim();
            return contenido.isEmpty() ? new JSONArray() : new JSONArray(contenido);
        }
    }
    private void escribirArchivo(JSONArray data) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(data.toString());
        }
    }

    private JSONObject ofertaAJson(Oferta o) {
        JSONObject jo = new JSONObject();
        jo.put("id", o.getId());
        jo.put("idVendedor", o.getIdVendedor());
        jo.put("idsTiquetes", o.getIdsTiquetes());
        jo.put("precioPublicado", o.getPrecioPublicado());
        jo.put("estado", o.getEstado().name());
        jo.put("fechaCreacion", o.getFechaCreacion().toString());
        jo.put("fechaCierre", o.getFechaCierre() == null ? JSONObject.NULL : o.getFechaCierre().toString());
        jo.put("idCompradorFinal", o.getIdCompradorFinal() == null ? JSONObject.NULL : o.getIdCompradorFinal());
        return jo;
    }

    private Oferta jsonAOferta(JSONObject jo) {
        String id = jo.getString("id");
        String idVendedor = jo.getString("idVendedor");
        List<Integer> idsT = new ArrayList<>();
        JSONArray arr = jo.getJSONArray("idsTiquetes");
        for (int i = 0; i < arr.length(); i++) idsT.add(arr.getInt(i));
        double precio = jo.getDouble("precioPublicado");
        Oferta o = Oferta.nueva(idVendedor, idsT, precio);

        try {
            var fId = Oferta.class.getDeclaredField("id");
            fId.setAccessible(true); fId.set(o, id);
        } catch (Exception ignore) {}
        
        try {
            var fEstado = Oferta.class.getDeclaredField("estado");
            fEstado.setAccessible(true); fEstado.set(o, Oferta.Estado.valueOf(jo.getString("estado")));
            var fCreac = Oferta.class.getDeclaredField("fechaCreacion");
            fCreac.setAccessible(true); fCreac.set(o, LocalDateTime.parse(jo.getString("fechaCreacion")));
            if (!jo.isNull("fechaCierre")) {
                var fCierre = Oferta.class.getDeclaredField("fechaCierre");
                fCierre.setAccessible(true); fCierre.set(o, LocalDateTime.parse(jo.getString("fechaCierre")));
            }
            if (!jo.isNull("idCompradorFinal")) {
                var fCompr = Oferta.class.getDeclaredField("idCompradorFinal");
                fCompr.setAccessible(true); fCompr.set(o, jo.getString("idCompradorFinal"));
            }
        } catch (Exception ignore) {}
        return o;
    }

    @Override
    public void guardarOferta(Oferta oferta) {
        try {
            JSONArray data = leerArchivo();
            // buscar si existe
            int idx = -1;
            for (int i = 0; i < data.length(); i++) {
                if (data.getJSONObject(i).getString("id").equals(oferta.getId())) { idx = i; break; }
            }
            JSONObject jo = ofertaAJson(oferta);
            if (idx >= 0) data.put(idx, jo); else data.put(jo);
            escribirArchivo(data);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando oferta", e);
        }
    }

    @Override
    public Oferta buscarOferta(String id) {
        try {
            JSONArray data = leerArchivo();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);
                if (jo.getString("id").equals(id)) return jsonAOferta(jo);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Error buscando oferta", e);
        }
    }

    @Override
    public List<Oferta> listarPublicadas() {
        try {
            JSONArray data = leerArchivo();
            List<Oferta> out = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);
                if ("PUBLICADA".equals(jo.getString("estado"))) out.add(jsonAOferta(jo));
            }
            return out;
        } catch (IOException e) {
            throw new RuntimeException("Error listando ofertas", e);
        }
    }
}

