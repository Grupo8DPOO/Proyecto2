package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import logica.Administrador;
import logica.Cliente;
import logica.Organizador;
import logica.Usuario;

public class PersistenciaUsuariosTxt implements IPersistenciaUsuarios {

    @Override
    public void salvarUsuarios(String ruta, List<Usuario> usuarios) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Usuario u : usuarios) {
                if (u instanceof Administrador) {
                    bw.write("Administrador;" + u.getCorreo() + ";" + u.getContrasena());
                } 
                else if (u instanceof Organizador) {
                    Organizador o = (Organizador) u;
                    bw.write("Organizador;" + o.getCorreo() + ";" + o.getContrasena() + ";" + o.getNombre());
                } 
                else if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    bw.write("Cliente;" + c.getCorreo() + ";" + c.getContrasena() + ";" + c.getSaldo());
                } 
                else {
                    bw.write("Usuario;" + u.getCorreo() + ";" + u.getContrasena());
                }
                bw.newLine();
            }
        } 
        catch (Exception e) {
            throw new Exception("Error al guardar usuarios");
        }
    }

    @Override
    public List<Usuario> cargarUsuarios(String ruta) throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes[0].equals("Administrador")) {
                    usuarios.add(new Administrador(partes[1], partes[2]));
                } 
                else if (partes[0].equals("Organizador")) {
                    usuarios.add(new Organizador(partes[1], partes[2], partes[3]));
                } 
                else if (partes[0].equals("Cliente")) {
                    Cliente c = new Cliente(partes[1], partes[2]);
                    c.setSaldo(Double.parseDouble(partes[3]));
                    usuarios.add(c);
                }
            }
        } 
        catch (Exception e) {
            throw new Exception("Error al cargar usuarios");
        }
        return usuarios;
    }
}
