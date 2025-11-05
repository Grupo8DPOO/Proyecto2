package marketplace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Oferta {

    public enum Estado { PUBLICADA, CANCELADA, REMOVIDA_ADMIN, VENDIDA }

    private String id;                
    private String idVendedor;        
    private List<Integer> idsTiquetes = new ArrayList<>();
    private double precioPublicado;
    private Estado estado = Estado.PUBLICADA;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaCierre;   
    private String idCompradorFinal;     


    public static Oferta nueva(String idVendedor, List<Integer> idsTiquetes, double precio) {
        Oferta o = new Oferta();
        o.id = UUID.randomUUID().toString();
        o.idVendedor = idVendedor;
        o.idsTiquetes.addAll(idsTiquetes);
        o.precioPublicado = precio;
        return o;
    }

    public void cancelar() { this.estado = Estado.CANCELADA; this.fechaCierre = LocalDateTime.now(); }
    public void removerPorAdmin() { this.estado = Estado.REMOVIDA_ADMIN; this.fechaCierre = LocalDateTime.now(); }
    public void marcarVendidaA(String idComprador) {
        this.estado = Estado.VENDIDA;
        this.idCompradorFinal = idComprador;
        this.fechaCierre = LocalDateTime.now();
    }


    public String getId() {
    	return id; 
    	}
    public String getIdVendedor() { 
    	return idVendedor; 
    	}
    public List<Integer> getIdsTiquetes() {
    	return idsTiquetes; 
    	}
    public double getPrecioPublicado() { 
    	return precioPublicado; 
    	}
    public Estado getEstado() { 
    	return estado; 
    	}
    public LocalDateTime getFechaCreacion() { 
    	return fechaCreacion; 
    	}
    public LocalDateTime getFechaCierre() { 
    	return fechaCierre; 
    	}
    public String getIdCompradorFinal() { 
    	return idCompradorFinal; 
    	}


    public void setPrecioPublicado(double p) { this.precioPublicado = p; }
}

