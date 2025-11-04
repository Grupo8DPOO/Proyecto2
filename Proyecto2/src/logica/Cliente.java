package logica;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    protected double saldo;
    protected List<Tiquete> tiquetesComprados;

    public Cliente(String correo, String contrasena) {
        super(correo, contrasena);
        this.saldo = 0.0;
        this.tiquetesComprados = new ArrayList<>();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Tiquete> getTiquetesComprados() {
        return tiquetesComprados;
    }

    public boolean comprarTiquete(Tiquete t) {
        try {
            if (t == null) {
            	throw new Exception("Tiquete no válido");
            }
            if (!t.isDisponible()) {
            	throw new Exception("Tiquete no disponible");
            }
            
            double total = t.calcularPrecioFinal();
            
            if (saldo < total) {
            	throw new Exception("Saldo insuficiente");
            }
            saldo -= total;
            boolean ok = t.marcarComoVendido(this);
            if (!ok) {
            	throw new Exception("Error al marcar el tiquete");
            }
            tiquetesComprados.add(t);
            Organizador org = t.getEvento() != null ? t.getEvento().getOrganizador() : null;
            if (org != null) {
            	org.registrarGanancia(t.getPrecio());
            }
            return true;
        } 
        catch (Exception e) {
            System.out.println("Error en compra");
            return false;
        }
    }

    public boolean transferirTiquete(Tiquete t, Cliente destino, String contrasenaTransferidor) {
        try {
            if (t == null || destino == null) {
            	throw new Exception("Datos inválidos");
            }
            if (!this.contrasena.equals(contrasenaTransferidor)) {
            	throw new Exception("Contraseña incorrecta");
            }
            if (!tiquetesComprados.contains(t)) {
            	throw new Exception("Tiquete no pertenece al cliente");
            }
            if (!t.isTransferible()) {
            	throw new Exception("Tiquete no transferible");
            }
            if (t instanceof PaqueteDeluxe) {
            	throw new Exception("Paquete Deluxe no se puede transferir");
            }
            tiquetesComprados.remove(t);
            destino.tiquetesComprados.add(t);
            t.setDueno(destino);
            return true;
        } 
        catch (Exception e) {
            System.out.println("Error en transferencia");
            e.printStackTrace();
            return false;
        }
    }

    public boolean solicitarReembolso(Tiquete t) {
        try {
            if (t == null) {
            	throw new Exception("Tiquete inválido");
            }
            if (!tiquetesComprados.contains(t)) {
            	throw new Exception("Tiquete no encontrado");
            }
            if (t.isVencido()) {
            	throw new Exception("Tiquete vencido");
            }
            double reembolso = t.getPrecio();
            tiquetesComprados.remove(t);
            saldo += reembolso;
            t.setDisponible(true);
            t.setDueno(null);
            return true;
        } 
        catch (Exception e) {
            System.out.println("Error en reembolso");
            e.printStackTrace();
            return false;
        }
    }
}
