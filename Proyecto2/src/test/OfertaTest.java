package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import marketplace.Oferta;

public class OfertaTest {

    private Oferta oferta;

    @BeforeEach
    void setUp() throws Exception {
        oferta = Oferta.nueva("vendedor@test.com", List.of(1, 2, 3), 120.0);
    }

    @AfterEach
    void tearDown() throws Exception {
        oferta = null;
    }

    @Test
    void testCreacionOferta() {
        assertEquals("vendedor@test.com", oferta.getIdVendedor(), 
            "El id del vendedor no es el esperado.");
        assertEquals(3, oferta.getIdsTiquetes().size(), 
            "La cantidad de tiquetes no es la esperada.");
        assertEquals(120.0, oferta.getPrecioPublicado(), 
            "El precio publicado no es el esperado.");
        assertEquals(Oferta.Estado.PUBLICADA, oferta.getEstado(), 
            "El estado inicial de la oferta debe ser PUBLICADA.");
    }

    @Test
    void testCancelarOferta() {
        oferta.cancelar();
        assertEquals(Oferta.Estado.CANCELADA, oferta.getEstado(), 
            "La oferta no cambió a estado CANCELADA.");
        assertNotNull(oferta.getFechaCierre(), 
            "La fecha de cierre no fue registrada al cancelar la oferta.");
    }

    @Test
    void testMarcarVendidaA() {
        oferta.marcarVendidaA("comprador@test.com");
        assertEquals(Oferta.Estado.VENDIDA, oferta.getEstado(), 
            "La oferta no cambió a estado VENDIDA.");
        assertEquals("comprador@test.com", oferta.getIdCompradorFinal(), 
            "El comprador final no fue registrado correctamente.");
    }
}

