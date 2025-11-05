package test;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.util.List;
import org.junit.jupiter.api.*;

import log.*;
import logica.*;
import marketplace.*;

public class ControlMarketplaceTest {

    private ControlMarketplace mkt;
    private AdminLog log;
    private Path ofertasFile;
    private Path logFile;
    private Cliente vendedor;
    private Cliente comprador;
    private Localidad locPrueba;

    @BeforeEach
    void setUp() throws Exception {
        ofertasFile = Paths.get("data/test_ofertas.json");
        logFile = Paths.get("data/test_audit.jsonl");
        Files.deleteIfExists(ofertasFile);
        Files.deleteIfExists(logFile);

        log = new AdminLog(new PersistenciaLogJson(logFile.toString()));
        mkt = new ControlMarketplace(new PersistenciaMarketplaceJson(ofertasFile.toString()), log);

        vendedor = new Cliente("vendedor@test.com", "1234");
        comprador = new Cliente("comprador@test.com", "abcd");

        locPrueba = new Localidad("General", false, 100.0, 50, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(ofertasFile);
        Files.deleteIfExists(logFile);
    }

    @Test
    void testPublicarYCancelarOferta() {
        TiqueteNumerado t1 = new TiqueteNumerado(locPrueba, 100.0, 1);

        Oferta o = mkt.publicarOferta(vendedor, List.of(t1), 100.0);
        assertEquals(Oferta.Estado.PUBLICADA, o.getEstado(),
                "La oferta debería iniciar en estado PUBLICADA.");

        mkt.cancelarOferta(vendedor, o.getId());
        Oferta cancelada = mkt.buscarOferta(o.getId());
        assertEquals(Oferta.Estado.CANCELADA, cancelada.getEstado(),
                "La oferta no cambió a estado CANCELADA.");
    }

    @Test
    void testCompraDirectaCambiaEstadoYGeneraLog() {
        TiqueteNumerado t2 = new TiqueteNumerado(locPrueba, 120.0, 2);

        Oferta o = mkt.publicarOferta(vendedor, List.of(t2), 120.0);
        mkt.comprarOferta(comprador, o.getId());

        Oferta vendida = mkt.buscarOferta(o.getId());
        assertEquals(Oferta.Estado.VENDIDA, vendida.getEstado(),
                "La oferta no cambió a estado VENDIDA tras la compra.");

        Administrador admin = new Administrador("admin@test.com", "0000");
        var entradas = log.listar(admin);
        assertTrue(entradas.stream().anyMatch(e -> e.getTipo() == LogEntrada.Tipo.COMPRA_DIRECTA),
                "No se registró la compra directa en el log.");
        assertTrue(entradas.stream().anyMatch(e -> e.getTipo() == LogEntrada.Tipo.VENTA_COMPLETADA),
                "No se registró la venta completada en el log.");
    }

    @Test
    void testContraofertaPropuestaYAceptada() {
        TiqueteNumerado t3 = new TiqueteNumerado(locPrueba, 150.0, 3);

        Oferta o = mkt.publicarOferta(vendedor, List.of(t3), 150.0);
        Contraoferta cf = mkt.proponerContraoferta(comprador, o.getId(), 100.0);
        mkt.aceptarContraoferta(vendedor, cf);

        assertEquals(Contraoferta.Estado.ACEPTADA, cf.getEstado(),
                "La contraoferta no cambió a estado ACEPTADA.");
        assertEquals(Oferta.Estado.VENDIDA, mkt.buscarOferta(o.getId()).getEstado(),
                "La oferta original no cambió a VENDIDA tras aceptar la contraoferta.");
    }
}

