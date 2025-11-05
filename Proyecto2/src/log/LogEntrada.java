package log;
import java.time.Instant;
import java.util.UUID;

public final class LogEntrada {
	public enum Tipo {
        OFERTA_PUBLICADA,
        OFERTA_CANCELADA,
        OFERTA_REMOVIDA_ADMIN,
        CONTRAOFERTA_PROPUESTA,
        CONTRAOFERTA_ACEPTADA,
        CONTRAOFERTA_RECHAZADA,
        COMPRA_DIRECTA,
        VENTA_COMPLETADA
    }

	private final String id;           
    private final String actorId;      
    private final Tipo tipo;           
    private final String detalle;      
    private final String tiempo;
    
    public LogEntrada(String actorId, Tipo tipo, String detalle) {
        this.id = UUID.randomUUID().toString();       
        this.actorId = actorId;
        this.tipo = tipo;
        this.detalle = detalle;
        this.tiempo = Instant.now().toString(); 
    }
    public LogEntrada(String id, String actorId, Tipo tipo, String detalle, String tiempo) {
        this.id = id;
        this.actorId = actorId;
        this.tipo = tipo;
        this.detalle = detalle;
        this.tiempo = tiempo;
    }
    
	public String getId() {
		return id;
	}

	public String getActorId() {
		return actorId;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public String getDetalle() {
		return detalle;
	}

	public String getTiempo() {
		return tiempo;
	}
    
    @Override
    public String toString() {
        return tiempo + " | " + tipo + " | actor=" + actorId + " | " + detalle;
    }
	
}
