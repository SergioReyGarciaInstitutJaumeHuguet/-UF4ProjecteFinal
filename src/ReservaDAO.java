import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per gestionar les operacions CRUD de reserves a la base de dades.
 */
public class ReservaDAO {

    private HabitacioDAO habitacioDAO;
    private ClientDAO clientDAO;

    /**
     * Constructor que inicialitza els DAO necessaris.
     */
    public ReservaDAO() {
        this.habitacioDAO = new HabitacioDAO();
        this.clientDAO = new ClientDAO();
    }

    /**
     * Afegeix una nova reserva a la base de dades.
     * @param reserva La reserva a afegir.
     * @return l'ID de la reserva afegida o -1 si hi ha hagut algun error.
     */
    public int afegirReserva(Reserva reserva) {
        String sql = "INSERT INTO reserves (numero_habitacio, id_client, data_entrada, data_sortida, total_a_pagar) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reserva.getHabitacio().getNumeroHabitacio());
            stmt.setInt(2, reserva.getClient().getIdClient());
            stmt.setDate(3, Date.valueOf(reserva.getDataEntrada()));
            stmt.setDate(4, Date.valueOf(reserva.getDataSortida()));
            stmt.setDouble(5, reserva.getTotalAPagar());

            int files = stmt.executeUpdate();

            if (files > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idReserva = generatedKeys.getInt(1);

                        // Actualitzar la disponibilitat de l'habitació
                        Habitacio habitacio = reserva.getHabitacio();
                        habitacio.setDisponible(false);
                        habitacioDAO.actualitzarHabitacio(habitacio);

                        return idReserva;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en afegir la reserva: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Cancel·la una reserva (elimina la reserva i marca l'habitació com a disponible).
     * @param idReserva L'ID de la reserva a cancel·lar.
     * @return true si s'ha cancel·lat correctament, false en cas contrari.
     */
    public boolean cancelarReserva(int idReserva) {
        // Primer, obtenim la reserva per actualitzar la disponibilitat de l'habitació
        Reserva reserva = obtenirReserva(idReserva);

        if (reserva == null) {
            return false;
        }

        // Eliminar la reserva
        String sql = "DELETE FROM reserves WHERE id_reserva = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);

            int files = stmt.executeUpdate();

            if (files > 0) {
                // Actualitzar la disponibilitat de l'habitació
                Habitacio habitacio = reserva.getHabitacio();
                habitacio.setDisponible(true);
                habitacioDAO.actualitzarHabitacio(habitacio);

                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error en cancel·lar la reserva: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obté una reserva pel seu ID.
     * @param idReserva L'ID de la reserva a obtenir.
     * @return La reserva o null si no s'ha trobat.
     */
    public Reserva obtenirReserva(int idReserva) {
        String sql = "SELECT * FROM reserves WHERE id_reserva = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReserva(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir la reserva: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obté totes les reserves actives (data de sortida >= avui).
     * @return Una llista amb totes les reserves actives.
     */
    public List<Reserva> obtenirReservesActives() {
        String sql = "SELECT * FROM reserves WHERE data_sortida >= CURRENT_DATE() ORDER BY data_entrada";
        List<Reserva> reserves = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reserves.add(mapResultSetToReserva(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir les reserves actives: " + e.getMessage());
        }

        return reserves;
    }

    /**
     * Obté totes les reserves d'un client específic.
     * @param idClient L'ID del client.
     * @return Una llista amb totes les reserves del client.
     */
    public List<Reserva> obtenirReservesClient(int idClient) {
        String sql = "SELECT * FROM reserves WHERE id_client = ? ORDER BY data_entrada";
        List<Reserva> reserves = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reserves.add(mapResultSetToReserva(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir les reserves del client: " + e.getMessage());
        }

        return reserves;
    }

    /**
     * Verifica si una habitació està disponible per reservar en un període de dates.
     * @param numeroHabitacio El número de l'habitació.
     * @param dataEntrada La data d'entrada.
     * @param dataSortida La data de sortida.
     * @return true si l'habitació està disponible, false en cas contrari.
     */
    public boolean esHabitacioDisponiblePerPeriode(int numeroHabitacio, LocalDate dataEntrada, LocalDate dataSortida) {
        String sql = "SELECT COUNT(*) FROM reserves WHERE numero_habitacio = ? AND " +
                "((data_entrada BETWEEN ? AND ?) OR " +
                "(data_sortida BETWEEN ? AND ?) OR " +
                "(data_entrada <= ? AND data_sortida >= ?))";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroHabitacio);
            stmt.setDate(2, Date.valueOf(dataEntrada));
            stmt.setDate(3, Date.valueOf(dataSortida));
            stmt.setDate(4, Date.valueOf(dataEntrada));
            stmt.setDate(5, Date.valueOf(dataSortida));
            stmt.setDate(6, Date.valueOf(dataEntrada));
            stmt.setDate(7, Date.valueOf(dataSortida));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Si no hi ha cap reserva en el període, l'habitació està disponible
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en comprovar la disponibilitat de l'habitació: " + e.getMessage());
        }

        return false;
    }

    /**
     * Converteix un ResultSet en un objecte Reserva.
     * @param rs El ResultSet amb les dades de la reserva.
     * @return L'objecte Reserva.
     * @throws SQLException Si hi ha algun problema amb l'accés a les dades.
     */
    private Reserva mapResultSetToReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(rs.getInt("id_reserva"));

        // Obtenir l'habitació i el client
        Habitacio habitacio = habitacioDAO.obtenirHabitacio(rs.getInt("numero_habitacio"));
        Client client = clientDAO.obtenirClient(rs.getInt("id_client"));

        reserva.setHabitacio(habitacio);
        reserva.setClient(client);
        reserva.setDataEntrada(rs.getDate("data_entrada").toLocalDate());
        reserva.setDataSortida(rs.getDate("data_sortida").toLocalDate());
        // No cal establir el total_a_pagar ja que es calcula automàticament al establir les dates i l'habitació

        return reserva;
    }
}