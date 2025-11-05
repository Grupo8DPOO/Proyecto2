package marketplace;

import java.time.LocalDateTime;
import java.util.UUID;

public class Contraoferta {

    public enum Estado { PENDIENTE, ACEPTADA, RECHAZADA }

    private String id;
    private String idOfertaOriginal;
    private String idComprador;
    private double precioPropuesto;
    private Estado estado = Estado.PENDIENTE;
    private LocalDateTime fechaPropuesta = LocalDateTime.now();
    private LocalDateTime fechaResolucion;

    public Contraoferta(String idOferta, String comprador, double precio) {
        this.id = UUID.randomUUID().toString();
        this.idOfertaOriginal = idOferta;
        this.idComprador = comprador;
        this.precioPropuesto = precio;
    }

    public void aceptar() { this.estado = Estado.ACEPTADA; this.fechaResolucion = LocalDateTime.now(); }
    public void rechazar() { this.estado = Estado.RECHAZADA; this.fechaResolucion = LocalDateTime.now(); }

    public String getId() { return id; }
    public String getIdOfertaOriginal() { return idOfertaOriginal; }
    public String getIdComprador() { return idComprador; }
    public double getPrecioPropuesto() { return precioPropuesto; }
    public Estado getEstado() { return estado; }
    public LocalDateTime getFechaPropuesta() { return fechaPropuesta; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
}
