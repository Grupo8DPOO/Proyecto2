package logica;

public class TiqueteNumerado extends Tiquete {
    private int numeroAsiento;

    public TiqueteNumerado(Localidad localidad, double precio, int numeroAsiento) {
        super(localidad, precio);
        this.numeroAsiento = numeroAsiento;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }
}
