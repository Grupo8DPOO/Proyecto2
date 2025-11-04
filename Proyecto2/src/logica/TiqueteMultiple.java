package logica;

import java.util.ArrayList;
import java.util.List;

public class TiqueteMultiple extends Tiquete {
    private List<Tiquete> tiquetesIncluidos;

    public TiqueteMultiple(Localidad localidad, double precio) {
        super(localidad, precio);
        this.tiquetesIncluidos = new ArrayList<>();
    }

    public void agregarTiquete(Tiquete t) {
        if (t != null && !tiquetesIncluidos.contains(t)) {
            tiquetesIncluidos.add(t);
        }
    }

    public List<Tiquete> getTiquetesIncluidos() {
        return tiquetesIncluidos;
    }
}
