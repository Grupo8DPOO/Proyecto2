package logica;

public abstract class Tiquete {
    private static int contador = 0;
    protected int id;
    protected double precio;
    protected boolean disponible;
    protected boolean transferible;
    protected Cliente dueno;
    protected Localidad localidad;

    public Tiquete(Localidad localidad, double precio) {
        this.id = ++contador;
        this.localidad = localidad;
        this.precio = precio;
        this.disponible = true;
        this.transferible = true;
        this.dueno = null;
    }

    public int getId() {
        return id;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean isTransferible() {
        return transferible;
    }

    public void setTransferible(boolean transferible) {
        this.transferible = transferible;
    }

    public Cliente getDueno() {
        return dueno;
    }

    public void setDueno(Cliente dueno) {
        this.dueno = dueno;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public Evento getEvento() {
        return localidad != null ? localidad.getEvento() : null;
    }
    
    
    
//TODO Revisar de aca pa abajo
    
    public double calcularPrecioFinal() {
        double precioFinal = precio + Administrador.getCargoFijo();
        Evento evento = getEvento();
        if (evento != null) {
            Double cargoTipo = Sistema.getAdministrador().getCargoPorTipo(evento.getTipo());
            precioFinal += (cargoTipo / 100.0) * precio;
        }
        return precioFinal;
    }

    public boolean marcarComoVendido(Cliente c) {
        if (disponible) {
            disponible = false;
            dueno = c;
            return true;
        }
        return false;
    }

    public boolean isVencido() {
        Evento e = getEvento();
        if (e == null) return false;
        return e.getFechaHora().isBefore(java.time.LocalDateTime.now());
    }
}
