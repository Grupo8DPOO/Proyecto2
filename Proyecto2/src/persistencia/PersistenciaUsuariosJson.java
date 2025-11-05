package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.Administrador;
import logica.Cliente;
import logica.Organizador;
import logica.Usuario;

public class PersistenciaUsuariosJson implements IPersistenciaUsuarios {

    private static final String USUARIOS = "usuarios";

    @Override
    public void salvarUsuarios(String archivo, List<Usuario> usuarios) throws IOException {
        JSONObject raiz = new JSONObject();
        JSONArray jUsuarios = new JSONArray();

        for (Usuario u : usuarios) {
            JSONObject jUsuario = new JSONObject();
            jUsuario.put("correo", u.getCorreo());
            jUsuario.put("contrasena", u.getContrasena());
            jUsuario.put("tipo", u.getClass().getSimpleName());

            if (u instanceof Cliente c) {
                jUsuario.put("saldo", c.getSaldo());
            } else if (u instanceof Organizador o) {
                jUsuario.put("nombre", o.getNombre());
                jUsuario.put("ganancias", o.consultarGanancias());
            }
            jUsuarios.put(jUsuario);
        }

        raiz.put(USUARIOS, jUsuarios);
        try (PrintWriter pw = new PrintWriter(archivo)) {
            raiz.write(pw, 2, 0);
        }
    }

    @Override
    public List<Usuario> cargarUsuarios(String archivo) throws IOException {
        List<Usuario> usuarios = new java.util.ArrayList<>();
        String contenido = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray jUsuarios = raiz.getJSONArray(USUARIOS);

        for (int i = 0; i < jUsuarios.length(); i++) {
            JSONObject jUsuario = jUsuarios.getJSONObject(i);
            String tipo = jUsuario.getString("tipo");
            String correo = jUsuario.getString("correo");
            String contrasena = jUsuario.getString("contrasena");

            switch (tipo) {
                case "Administrador" -> usuarios.add(new Administrador(correo, contrasena));
                case "Organizador" -> {
                    String nombre = jUsuario.optString("nombre", "");
                    Organizador o = new Organizador(correo, contrasena, nombre);
                    o.registrarGanancia(jUsuario.optDouble("ganancias", 0.0));
                    usuarios.add(o);
                }
                case "Cliente" -> {
                    Cliente c = new Cliente(correo, contrasena);
                    c.setSaldo(jUsuario.optDouble("saldo", 0.0));
                    usuarios.add(c);
                }
            }
        }
        return usuarios;
    }
}
