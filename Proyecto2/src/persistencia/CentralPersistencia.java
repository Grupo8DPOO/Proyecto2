package persistencia;

public class CentralPersistencia {
    public IPersistenciaUsuarios getPersistenciaUsuarios(String tipo) {
        if (tipo.equalsIgnoreCase("TXT")) {
            return new PersistenciaUsuariosTxt();
        }
        return null;
    }

    public IPersistenciaEventos getPersistenciaEventos(String tipo) {
        if (tipo.equalsIgnoreCase("TXT")) {
            return new PersistenciaEventosTxt();
        }
        return null;
    }
}
