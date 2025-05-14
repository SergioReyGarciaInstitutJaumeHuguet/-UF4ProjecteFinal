import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per gestionar les operacions CRUD d'habitacions a la base de dades.
 */
public class HabitacioDAO {

    /**
     * Afegeix una nova habitació a la base de dades.
     * @param habitacio L'habitació a afegir.
     * @return true si s'ha afegit correctament, false en cas contrari.
     */
    public boolean afegirHabitacio(Habitacio habitacio) {
        String sql = "INSERT INTO habitacions (numero_habitacio, tipus, preu_per_nit, disponible) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitacio.getNumeroHabitacio());
            stmt.setString(2, habitacio.getTipus());
            stmt.setDouble(3, habitacio.getPreuPerNit());
            stmt.setBoolean(4, habitacio.isDisponible());

            int files = stmt.executeUpdate();
            return files > 0;

        } catch (SQLException e) {
            System.err.println("Error en afegir l'habitació: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una habitació de la base de dades.
     * @param numeroHabitacio El número de l'habitació a eliminar.
     * @return true si s'ha eliminat correctament, false en cas contrari.
     */
    public boolean eliminarHabitacio(int numeroHabitacio) {
        String sql = "DELETE FROM habitacions WHERE numero_habitacio = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroHabitacio);

            int files = stmt.executeUpdate();
            return files > 0;

        } catch (SQLException e) {
            System.err.println("Error en eliminar l'habitació: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualitza la informació d'una habitació a la base de dades.
     * @param habitacio L'habitació amb la informació actualitzada.
     * @return true si s'ha actualitzat correctament, false en cas contrari.
     */
    public boolean actualitzarHabitacio(Habitacio habitacio) {
        String sql = "UPDATE habitacions SET tipus = ?, preu_per_nit = ?, disponible = ? WHERE numero_habitacio = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, habitacio.getTipus());
            stmt.setDouble(2, habitacio.getPreuPerNit());
            stmt.setBoolean(3, habitacio.isDisponible());
            stmt.setInt(4, habitacio.getNumeroHabitacio());

            int files = stmt.executeUpdate();
            return files > 0;

        } catch (SQLException e) {
            System.err.println("Error en actualitzar l'habitació: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obté una habitació pel seu número.
     * @param numeroHabitacio El número de l'habitació a obtenir.
     * @return L'habitació o null si no s'ha trobat.
     */
    public Habitacio obtenirHabitacio(int numeroHabitacio) {
        String sql = "SELECT * FROM habitacions WHERE numero_habitacio = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroHabitacio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHabitacio(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir l'habitació: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obté totes les habitacions de la base de dades.
     * @return Una llista amb totes les habitacions.
     */
    public List<Habitacio> obtenirTotesLesHabitacions() {
        String sql = "SELECT * FROM habitacions";
        List<Habitacio> habitacions = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                habitacions.add(mapResultSetToHabitacio(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir totes les habitacions: " + e.getMessage());
        }

        return habitacions;
    }

    /**
     * Obté totes les habitacions disponibles de la base de dades.
     * @return Una llista amb totes les habitacions disponibles.
     */
    public List<Habitacio> obtenirHabitacionsDisponibles() {
        String sql = "SELECT * FROM habitacions WHERE disponible = TRUE";
        List<Habitacio> habitacions = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                habitacions.add(mapResultSetToHabitacio(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenir les habitacions disponibles: " + e.getMessage());
        }

        return habitacions;
    }

    /**
     * Converteix un ResultSet en un objecte Habitacio.
     * @param rs El ResultSet amb les dades de l'habitació.
     * @return L'objecte Habitacio.
     * @throws SQLException Si hi ha algun problema amb l'accés a les dades.
     */
    private Habitacio mapResultSetToHabitacio(ResultSet rs) throws SQLException {
        Habitacio habitacio = new Habitacio();
        habitacio.setNumeroHabitacio(rs.getInt("numero_habitacio"));
        habitacio.setTipus(rs.getString("tipus"));
        habitacio.setPreuPerNit(rs.getDouble("preu_per_nit"));
        habitacio.setDisponible(rs.getBoolean("disponible"));
        return habitacio;
    }
}