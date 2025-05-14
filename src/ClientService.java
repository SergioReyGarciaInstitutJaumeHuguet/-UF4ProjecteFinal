import java.time.LocalDate;
import java.util.List;

/**
 * Servei que gestiona les operacions de negoci relacionades amb els clients.
 */
public class ClientService {

    private ClientDAO clientDAO;

    /**
     * Constructor que inicialitza el DAO.
     */
    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    /**
     * Afegeix un nou client.
     * @param nom El nom del client.
     * @param cognoms Els cognoms del client.
     * @param dataNaixement La data de naixement del client.
     * @param email L'email del client.
     * @param telefon El telèfon del client.
     * @return l'ID del client afegit o -1 si hi ha hagut algun error.
     */
    public int afegirClient(String nom, String cognoms, LocalDate dataNaixement, String email, String telefon) {
        // Validacions
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Error: El nom no pot estar buit");
            return -1;
        }

        if (cognoms == null || cognoms.trim().isEmpty()) {
            System.err.println("Error: Els cognoms no poden estar buits");
            return -1;
        }

        if (dataNaixement == null) {
            System.err.println("Error: La data de naixement no pot estar buida");
            return -1;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            System.err.println("Error: L'email no és vàlid");
            return -1;
        }

        if (telefon == null || telefon.trim().isEmpty()) {
            System.err.println("Error: El telèfon no pot estar buit");
            return -1;
        }

        Client client = new Client(0, nom, cognoms, dataNaixement, email, telefon);
        return clientDAO.afegirClient(client);
    }

    /**
     * Elimina un client.
     * @param idClient L'ID del client a eliminar.
     * @return true si s'ha eliminat correctament, false en cas contrari.
     */
    public boolean eliminarClient(int idClient) {
        // Verificar que el client existeixi
        if (clientDAO.obtenirClient(idClient) == null) {
            System.err.println("Error: No existeix cap client amb l'ID " + idClient);
            return false;
        }

        return clientDAO.eliminarClient(idClient);
    }

    /**
     * Actualitza la informació d'un client.
     * @param idClient L'ID del client.
     * @param nom El nou nom del client.
     * @param cognoms Els nous cognoms del client.
     * @param dataNaixement La nova data de naixement del client.
     * @param email El nou email del client.
     * @param telefon El nou telèfon del client.
     * @return true si s'ha actualitzat correctament, false en cas contrari.
     */
    public boolean actualitzarClient(int idClient, String nom, String cognoms, LocalDate dataNaixement, String email, String telefon) {
        // Verificar que el client existeixi
        Client client = clientDAO.obtenirClient(idClient);
        if (client == null) {
            System.err.println("Error: No existeix cap client amb l'ID " + idClient);
            return false;
        }

        // Validacions
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Error: El nom no pot estar buit");
            return false;
        }

        if (cognoms == null || cognoms.trim().isEmpty()) {
            System.err.println("Error: Els cognoms no poden estar buits");
            return false;
        }

        if (dataNaixement == null) {
            System.err.println("Error: La data de naixement no pot estar buida");
            return false;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            System.err.println("Error: L'email no és vàlid");
            return false;
        }

        if (telefon == null || telefon.trim().isEmpty()) {
            System.err.println("Error: El telèfon no pot estar buit");
            return false;
        }

        client.setNom(nom);
        client.setCognoms(cognoms);
        client.setDataNaixement(dataNaixement);
        client.setEmail(email);
        client.setTelefon(telefon);

        return clientDAO.actualitzarClient(client);
    }

    /**
     * Obté un client pel seu ID.
     * @param idClient L'ID del client.
     * @return El client o null si no s'ha trobat.
     */
    public Client obtenirClient(int idClient) {
        return clientDAO.obtenirClient(idClient);
    }

    /**
     * Obté tots els clients.
     * @return Una llista amb tots els clients.
     */
    public List<Client> obtenirTotsElsClients() {
        return clientDAO.obtenirTotsElsClients();
    }
}