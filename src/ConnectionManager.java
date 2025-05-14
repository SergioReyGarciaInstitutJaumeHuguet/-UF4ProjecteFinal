import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe que gestiona la connexió amb la base de dades.
 */
public class ConnectionManager {

    // Paràmetres de connexió a la base de dades
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_reserves";
    private static final String USER = "root";
    private static final String PASSWORD = "2012";

    private static Connection connection = null;

    /**
     * Obté una connexió a la base de dades.
     * @return La connexió a la base de dades.
     * @throws SQLException Si hi ha algun problema amb la connexió.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carregar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establir la connexió
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("No s'ha trobat el driver de MySQL", e);
            }
        }
        return connection;
    }

    /**
     * Tanca la connexió a la base de dades.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error en tancar la connexió: " + e.getMessage());
        }
    }

    /**
     * Script SQL per crear les taules de la base de dades.
     * @return String amb les comandes SQL per crear les taules.
     */
    public static String getCreateTablesScript() {
        return "CREATE DATABASE IF NOT EXISTS hotel_reserves;\n" +
                "USE hotel_reserves;\n\n" +

                "CREATE TABLE IF NOT EXISTS habitacions (\n" +
                "  numero_habitacio INT PRIMARY KEY,\n" +
                "  tipus VARCHAR(50) NOT NULL,\n" +
                "  preu_per_nit DOUBLE NOT NULL,\n" +
                "  disponible BOOLEAN NOT NULL DEFAULT TRUE\n" +
                ");\n\n" +

                "CREATE TABLE IF NOT EXISTS clients (\n" +
                "  id_client INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "  nom VARCHAR(100) NOT NULL,\n" +
                "  cognoms VARCHAR(200) NOT NULL,\n" +
                "  data_naixement DATE NOT NULL,\n" +
                "  email VARCHAR(200) UNIQUE NOT NULL,\n" +
                "  telefon VARCHAR(20) NOT NULL\n" +
                ");\n\n" +

                "CREATE TABLE IF NOT EXISTS reserves (\n" +
                "  id_reserva INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "  numero_habitacio INT NOT NULL,\n" +
                "  id_client INT NOT NULL,\n" +
                "  data_entrada DATE NOT NULL,\n" +
                "  data_sortida DATE NOT NULL,\n" +
                "  total_a_pagar DOUBLE NOT NULL,\n" +
                "  FOREIGN KEY (numero_habitacio) REFERENCES habitacions(numero_habitacio),\n" +
                "  FOREIGN KEY (id_client) REFERENCES clients(id_client)\n" +
                ");";
    }
}