package logica;

public class PaqueteDeluxe extends Tiquete {
    private String beneficios;

    public PaqueteDeluxe(Localidad localidad, double precio, String beneficios) {
        super(localidad, precio);
        this.beneficios = beneficios;
        this.transferible = false;
    }

    public String getBeneficios() {
        return beneficios;
    }
}
