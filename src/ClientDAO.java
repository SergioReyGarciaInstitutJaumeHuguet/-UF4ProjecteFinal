import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per gestionar les operacions CRUD de clients a la base de dades.
 */
public class ClientDAO {

    /**
     * Afegeix un nou client a la base de dades.
     * @param client El client a afegir.
     * @return l'ID del client afegit o -1 si hi ha hagut algun error.
     */
    public int afegirClient(Client client) {
        String sql = "INSERT INTO clients (nom, cognoms, data_naixement, email, telefon) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getCognoms());
            stmt.setDate(3, Date.valueOf(client.getDataNaixement()));
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getTelefon());

            int files = stmt.executeUpdate();

            if (files > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en afegir el client: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Elimina un client de la base de dades.
     * @param idClient L'ID del client a eliminar.
     * @return true si s'ha eliminat correctament, false en cas contrari.
     */
    public boolean eliminarClient(int idClient) {
        String sql = "DELETE FROM clients WHERE id_client = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);

            int files = stmt.executeUpdate();
            return files > 0;

        } catch (SQLException e) {
            System.err.println("Error en eliminar el client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualitza la informació d'un client a la base de dades.
     * @param client El client amb la informació actualitzada.
     * @return true si s'ha actualitzat correctament, false en cas contrari.
     */
    public boolean actualitzarClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, cognoms = ?, data_naixement = ?, email = ?, telefon = ? WHERE id_client = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getCognoms());
            stmt.setDate(3, Date.valueOf(client.getDataNaixement()));
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getTelefon());
            stmt.setInt(6, client.getIdClient());

            int files = stmt.executeUpdate();
            return files > 0;

        } catch (SQLException e) {
            System.err.println("Error en actualitzar el client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obté un client pel seu ID.
     * @param idClient L'ID del client a obtenir.
     * @return El client o null si no s'ha trobat.
     */
    public Client obtenirClient(int idClient) {
        String sql = "SELECT * FROM clients WHERE id_client = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir el client: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obté tots els clients de la base de dades.
     * @return Una llista amb tots els clients.
     */
    public List<Client> obtenirTotsElsClients() {
        String sql = "SELECT * FROM clients";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir tots els clients: " + e.getMessage());
        }

        return clients;
    }

    /**
     * Converteix un ResultSet en un objecte Client.
     * @param rs El ResultSet amb les dades del client.
     * @return L'objecte Client.
     * @throws SQLException Si hi ha algun problema amb l'accés a les dades.
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setIdClient(rs.getInt("id_client"));
        client.setNom(rs.getString("nom"));
        client.setCognoms(rs.getString("cognoms"));
        client.setDataNaixement(rs.getDate("data_naixement").toLocalDate());
        client.setEmail(rs.getString("email"));
        client.setTelefon(rs.getString("telefon"));
        return client;
    }
}