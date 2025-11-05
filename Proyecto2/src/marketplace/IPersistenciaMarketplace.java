package marketplace;

import java.util.List;

public interface IPersistenciaMarketplace {
    void guardarOferta(Oferta oferta);       
    Oferta buscarOferta(String id);
    List<Oferta> listarPublicadas();
}
