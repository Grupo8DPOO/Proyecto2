package persistencia;

public class CentralPersistencia {

    private final IPersistenciaUsuarios persistenciaUsuarios;
    private final IPersistenciaEventos persistenciaEventos;
    private final IPersistenciaVenues persistenciaVenues;

    public CentralPersistencia() {
        this.persistenciaUsuarios = new PersistenciaUsuariosJson();
        this.persistenciaEventos = new PersistenciaEventosJson();
        this.persistenciaVenues = new PersistenciaVenuesJson();
    }

    public IPersistenciaUsuarios getPersistenciaUsuarios() {
        return persistenciaUsuarios;
    }

    public IPersistenciaEventos getPersistenciaEventos() {
        return persistenciaEventos;
    }

    public IPersistenciaVenues getPersistenciaVenues() {
        return persistenciaVenues;
    }
}

