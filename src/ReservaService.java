import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

/**
 * Servei que gestiona les operacions de negoci relacionades amb les reserves.
 */
public class ReservaService {

    private ReservaDAO reservaDAO;
    private HabitacioService habitacioService;
    private ClientService clientService;

    /**
     * Constructor que inicialitza els DAO i serveis necessaris.
     */
    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.habitacioService = new HabitacioService();
        this.clientService = new ClientService();
    }

    /**
     * Realitza una nova reserva.
     * @param numeroHabitacio El número de l'habitació a reservar.
     * @param idClient L'ID del client que fa la reserva.
     * @param dataEntrada La data d'entrada.
     * @param dataSortida La data de sortida.
     * @return l'ID de la reserva realitzada o -1 si hi ha hagut algun error.
     */
    public int realitzarReserva(int numeroHabitacio, int idClient, LocalDate dataEntrada, LocalDate dataSortida) {
        // Verificar que l'habitació existeixi
        Habitacio habitacio = habitacioService.obtenirHabitacio(numeroHabitacio);
        if (habitacio == null) {
            System.err.println("Error: No existeix cap habitació amb el número " + numeroHabitacio);
            return -1;
        }

        // Verificar que el client existeixi
        Client client = clientService.obtenirClient(idClient);
        if (client == null) {
            System.err.println("Error: No existeix cap client amb l'ID " + idClient);
            return -1;
        }

        // Verificar que les dates siguin vàlides
        if (dataEntrada == null || dataSortida == null) {
            System.err.println("Error: Les dates d'entrada i sortida no poden estar buides");
            return -1;
        }

        if (dataEntrada.isAfter(dataSortida)) {
            System.err.println("Error: La data d'entrada no pot ser posterior a la data de sortida");
            return -1;
        }

        if (dataEntrada.isBefore(LocalDate.now())) {
            System.err.println("Error: La data d'entrada no pot ser anterior a la data actual");
            return -1;
        }

        // Verificar que l'habitació estigui disponible per al període
        if (!habitacio.isDisponible()) {
            System.err.println("Error: L'habitació no està disponible");
            return -1;
        }

        if (!reservaDAO.esHabitacioDisponiblePerPeriode(numeroHabitacio, dataEntrada, dataSortida)) {
            System.err.println("Error: L'habitació no està disponible per al període seleccionat");
            return -1;
        }

        // Crear la reserva
        Reserva reserva = new Reserva(0, habitacio, client, dataEntrada, dataSortida);
        return reservaDAO.afegirReserva(reserva);
    }

    /**
     * Cancel·la una reserva.
     * @param idReserva L'ID de la reserva a cancel·lar.
     * @return true si s'ha cancel·lat correctament, false en cas contrari.
     */
    public boolean cancelarReserva(int idReserva) {
        // Verificar que la reserva existeixi
        Reserva reserva = reservaDAO.obtenirReserva(idReserva);
        if (reserva == null) {
            System.err.println("Error: No existeix cap reserva amb l'ID " + idReserva);
            return false;
        }

        return reservaDAO.cancelarReserva(idReserva);
    }

    /**
     * Obté una reserva pel seu ID.
     * @param idReserva L'ID de la reserva.
     * @return La reserva o null si no s'ha trobat.
     */
    public Reserva obtenirReserva(int idReserva) {
        return reservaDAO.obtenirReserva(idReserva);
    }

    /**
     * Obté totes les reserves actives.
     * @return Una llista amb totes les reserves actives.
     */
    public List<Reserva> obtenirReservesActives() {
        return reservaDAO.obtenirReservesActives();
    }

    /**
     * Obté totes les reserves d'un client específic.
     * @param idClient L'ID del client.
     * @return Una llista amb totes les reserves del client.
     */
    public List<Reserva> obtenirReservesClient(int idClient) {
        // Verificar que el client existeixi
        Client client = clientService.obtenirClient(idClient);
        if (client == null) {
            System.err.println("Error: No existeix cap client amb l'ID " + idClient);
            return Collections.emptyList();
        }

        return reservaDAO.obtenirReservesClient(idClient);
    }
}