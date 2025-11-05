package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import log.*;
import logica.Administrador;
import logica.Cliente;
import java.nio.file.*;

public class AdminLogTest {

    private AdminLog log;
    private Path filePath;

    @BeforeEach
    void setUp() throws Exception {
        filePath = Paths.get("data/test_audit.jsonl");
        Files.deleteIfExists(filePath);
        log = new AdminLog(new PersistenciaLogJson(filePath.toString()));
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(filePath);
    }

    @Test
    void testLogYLecturaPorAdmin() {
        log.log("cliente@test.com", LogEntrada.Tipo.OFERTA_PUBLICADA, "detalle=prueba");
        log.log("cliente@test.com", LogEntrada.Tipo.OFERTA_CANCELADA, "detalle=cancelado");

        Administrador admin = new Administrador("admin@test.com", "1234");
        List<LogEntrada> entradas = log.listar(admin);

        assertEquals(2, entradas.size(), 
            "La cantidad de entradas en el log no es la esperada.");
        assertEquals(LogEntrada.Tipo.OFERTA_PUBLICADA, entradas.get(0).getTipo(), 
            "El tipo de la primera entrada no coincide con OFERTA_PUBLICADA.");
    }

    @Test
    void testSoloAdminPuedeConsultarLog() {
        log.log("cliente@test.com", LogEntrada.Tipo.OFERTA_PUBLICADA, "detalle=prueba");

        Cliente c = new Cliente("cliente@test.com", "1234");
        assertThrows(SecurityException.class, () -> log.listar(c),
            "Un cliente no debería poder consultar el log");
    }
}
