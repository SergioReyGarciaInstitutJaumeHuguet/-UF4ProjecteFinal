import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfície de línia d'ordres (CLI) per interactuar amb l'aplicació.
 */
public class HotelCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final HabitacioService habitacioService = new HabitacioService();
    private static final ClientService clientService = new ClientService();
    private static final ReservaService reservaService = new ReservaService();

    /**
     * Mètode principal de l'aplicació.
     * @param args Arguments de la línia d'ordres.
     */
    public static void main(String[] args) {
        setupDatabase();

        boolean sortir = false;

        while (!sortir) {
            mostrarMenuPrincipal();
            int opcio = llegirOpcio();

            switch (opcio) {
                case 1:
                    gestionarHabitacions();
                    break;
                case 2:
                    gestionarClients();
                    break;
                case 3:
                    gestionarReserves();
                    break;
                case 0:
                    sortir = true;
                    System.out.println("Gràcies per utilitzar l'aplicació. Adéu!");
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }

        // Tancar la connexió a la base de dades en sortir de l'aplicació
        ConnectionManager.closeConnection();
    }

    /**
     * Configura la base de dades.
     */
    private static void setupDatabase() {
        try {
            // Obtenir la connexió
            Connection conn = ConnectionManager.getConnection();

            // Crear les taules si no existeixen
            String createTablesScript = ConnectionManager.getCreateTablesScript();
            Statement stmt = conn.createStatement();

            // Executar el script SQL
            String[] statements = createTablesScript.split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }

            System.out.println("Base de dades configurada correctament.");

        } catch (SQLException e) {
            System.err.println("Error en configurar la base de dades: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Mostra el menú principal de l'aplicació.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== GESTIÓ DE RESERVES D'HOTEL =====");
        System.out.println("1. Gestió d'Habitacions");
        System.out.println("2. Gestió de Clients");
        System.out.println("3. Gestió de Reserves");
        System.out.println("0. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    /**
     * Llegeix una opció del teclat.
     * @return L'opció seleccionada.
     */
    private static int llegirOpcio() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Gestiona les operacions relacionades amb les habitacions.
     */
    private static void gestionarHabitacions() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n===== GESTIÓ D'HABITACIONS =====");
            System.out.println("1. Afegir una nova habitació");
            System.out.println("2. Eliminar una habitació");
            System.out.println("3. Actualitzar una habitació");
            System.out.println("4. Consultar totes les habitacions");
            System.out.println("5. Consultar habitacions disponibles");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Selecciona una opció: ");

            int opcio = llegirOpcio();

            switch (opcio) {
                case 1:
                    afegirHabitacio();
                    break;
                case 2:
                    eliminarHabitacio();
                    break;
                case 3:
                    actualitzarHabitacio();
                    break;
                case 4:
                    consultarTotesLesHabitacions();
                    break;
                case 5:
                    consultarHabitacionsDisponibles();
                    break;
                case 0:
                    tornar = true;
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    /**
     * Gestiona les operacions relacionades amb els clients.
     */
    private static void gestionarClients() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n===== GESTIÓ DE CLIENTS =====");
            System.out.println("1. Afegir un nou client");
            System.out.println("2. Eliminar un client");
            System.out.println("3. Actualitzar un client");
            System.out.println("4. Consultar tots els clients");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Selecciona una opció: ");

            int opcio = llegirOpcio();

            switch (opcio) {
                case 1:
                    afegirClient();
                    break;
                case 2:
                    eliminarClient();
                    break;
                case 3:
                    actualitzarClient();
                    break;
                case 4:
                    consultarTotsElsClients();
                    break;
                case 0:
                    tornar = true;
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    /**
     * Gestiona les operacions relacionades amb les reserves.
     */
    private static void gestionarReserves() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n===== GESTIÓ DE RESERVES =====");
            System.out.println("1. Realitzar una nova reserva");
            System.out.println("2. Cancel·lar una reserva");
            System.out.println("3. Consultar totes les reserves actives");
            System.out.println("4. Consultar les reserves d'un client");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Selecciona una opció: ");

            int opcio = llegirOpcio();

            switch (opcio) {
                case 1:
                    realitzarReserva();
                    break;
                case 2:
                    cancelarReserva();
                    break;
                case 3:
                    consultarReservesActives();
                    break;
                case 4:
                    consultarReservesClient();
                    break;
                case 0:
                    tornar = true;
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }

    // Mètodes per gestionar les habitacions

    /**
     * Afegeix una nova habitació.
     */
    private static void afegirHabitacio() {
        System.out.println("\n--- Afegir una nova habitació ---");

        System.out.print("Número d'habitació: ");
        int numeroHabitacio = llegirOpcio();

        System.out.print("Tipus d'habitació (individual, doble, suite...): ");
        String tipus = scanner.nextLine();

        System.out.print("Preu per nit (€): ");
        double preuPerNit = llegirDouble();

        boolean resultat = habitacioService.afegirHabitacio(numeroHabitacio, tipus, preuPerNit);

        if (resultat) {
            System.out.println("Habitació afegida correctament.");
        } else {
            System.out.println("No s'ha pogut afegir l'habitació.");
        }
    }

    /**
     * Elimina una habitació.
     */
    private static void eliminarHabitacio() {
        System.out.println("\n--- Eliminar una habitació ---");

        System.out.print("Número d'habitació a eliminar: ");
        int numeroHabitacio = llegirOpcio();

        boolean resultat = habitacioService.eliminarHabitacio(numeroHabitacio);

        if (resultat) {
            System.out.println("Habitació eliminada correctament.");
        } else {
            System.out.println("No s'ha pogut eliminar l'habitació.");
        }
    }

    /**
     * Actualitza una habitació.
     */
    private static void actualitzarHabitacio() {
        System.out.println("\n--- Actualitzar una habitació ---");

        System.out.print("Número d'habitació a actualitzar: ");
        int numeroHabitacio = llegirOpcio();

        Habitacio habitacio = habitacioService.obtenirHabitacio(numeroHabitacio);

        if (habitacio == null) {
            System.out.println("No s'ha trobat cap habitació amb aquest número.");
            return;
        }

        System.out.println("Habitació actual: " + habitacio);

        System.out.print("Nou tipus d'habitació (deixar en blanc per mantenir l'actual): ");
        String tipus = scanner.nextLine();
        if (tipus.isEmpty()) {
            tipus = habitacio.getTipus();
        }

        System.out.print("Nou preu per nit (€) (deixar en blanc per mantenir l'actual): ");
        String preuStr = scanner.nextLine();
        double preuPerNit = preuStr.isEmpty() ? habitacio.getPreuPerNit() : Double.parseDouble(preuStr);

        System.out.print("Disponible (s/n) (deixar en blanc per mantenir l'actual): ");
        String disponibleStr = scanner.nextLine();
        boolean disponible = disponibleStr.isEmpty() ? habitacio.isDisponible() :
                disponibleStr.equalsIgnoreCase("s");

        boolean resultat = habitacioService.actualitzarHabitacio(numeroHabitacio, tipus, preuPerNit, disponible);

        if (resultat) {
            System.out.println("Habitació actualitzada correctament.");
        } else {
            System.out.println("No s'ha pogut actualitzar l'habitació.");
        }
    }

    /**
     * Consulta totes les habitacions.
     */
    private static void consultarTotesLesHabitacions() {
        System.out.println("\n--- Totes les habitacions ---");

        List<Habitacio> habitacions = habitacioService.obtenirTotesLesHabitacions();

        if (habitacions.isEmpty()) {
            System.out.println("No hi ha cap habitació registrada.");
        } else {
            for (Habitacio habitacio : habitacions) {
                System.out.println(habitacio);
            }
        }
    }

    /**
     * Consulta les habitacions disponibles.
     */
    private static void consultarHabitacionsDisponibles() {
        System.out.println("\n--- Habitacions disponibles ---");

        List<Habitacio> habitacionsDisponibles = habitacioService.obtenirHabitacionsDisponibles();

        if (habitacionsDisponibles.isEmpty()) {
            System.out.println("No hi ha cap habitació disponible.");
        } else {
            for (Habitacio habitacio : habitacionsDisponibles) {
                System.out.println(habitacio);
            }
        }
    }

    // Mètodes per gestionar els clients

    /**
     * Afegeix un nou client.
     */
    private static void afegirClient() {
        System.out.println("\n--- Afegir un nou client ---");

        System.out.print("Nom: ");
        String nom = scanner.nextLine();

        System.out.print("Cognoms: ");
        String cognoms = scanner.nextLine();

        System.out.print("Data de naixement (dd/mm/aaaa): ");
        LocalDate dataNaixement = llegirData();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Telèfon: ");
        String telefon = scanner.nextLine();

        int idClient = clientService.afegirClient(nom, cognoms, dataNaixement, email, telefon);

        if (idClient > 0) {
            System.out.println("Client afegit correctament amb ID: " + idClient);
        } else {
            System.out.println("No s'ha pogut afegir el client.");
        }
    }

    /**
     * Elimina un client.
     */
    private static void eliminarClient() {
        System.out.println("\n--- Eliminar un client ---");

        System.out.print("ID del client a eliminar: ");
        int idClient = llegirOpcio();

        boolean resultat = clientService.eliminarClient(idClient);

        if (resultat) {
            System.out.println("Client eliminat correctament.");
        } else {
            System.out.println("No s'ha pogut eliminar el client.");
        }
    }

    /**
     * Actualitza un client.
     */
    private static void actualitzarClient() {
        System.out.println("\n--- Actualitzar un client ---");

        System.out.print("ID del client a actualitzar: ");
        int idClient = llegirOpcio();

        Client client = clientService.obtenirClient(idClient);

        if (client == null) {
            System.out.println("No s'ha trobat cap client amb aquest ID.");
            return;
        }

        System.out.println("Client actual: " + client);

        System.out.print("Nou nom (deixar en blanc per mantenir l'actual): ");
        String nom = scanner.nextLine();
        if (nom.isEmpty()) {
            nom = client.getNom();
        }

        System.out.print("Nous cognoms (deixar en blanc per mantenir l'actual): ");
        String cognoms = scanner.nextLine();
        if (cognoms.isEmpty()) {
            cognoms = client.getCognoms();
        }

        System.out.print("Nova data de naixement (dd/mm/aaaa) (deixar en blanc per mantenir l'actual): ");
        String dataStr = scanner.nextLine();
        LocalDate dataNaixement = dataStr.isEmpty() ? client.getDataNaixement() : llegirData(dataStr);

        System.out.print("Nou email (deixar en blanc per mantenir l'actual): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) {
            email = client.getEmail();
        }

        System.out.print("Nou telèfon (deixar en blanc per mantenir l'actual): ");
        String telefon = scanner.nextLine();
        if (telefon.isEmpty()) {
            telefon = client.getTelefon();
        }

        boolean resultat = clientService.actualitzarClient(idClient, nom, cognoms, dataNaixement, email, telefon);

        if (resultat) {
            System.out.println("Client actualitzat correctament.");
        } else {
            System.out.println("No s'ha pogut actualitzar el client.");
        }
    }

    /**
     * Consulta tots els clients.
     */
    private static void consultarTotsElsClients() {
        System.out.println("\n--- Tots els clients ---");

        List<Client> clients = clientService.obtenirTotsElsClients();

        if (clients.isEmpty()) {
            System.out.println("No hi ha cap client registrat.");
        } else {
            for (Client client : clients) {
                System.out.println(client);
            }
        }
    }

    // Mètodes per gestionar les reserves

    /**
     * Realitza una nova reserva.
     */
    private static void realitzarReserva() {
        System.out.println("\n--- Realitzar una nova reserva ---");

        // Mostrar les habitacions disponibles
        List<Habitacio> habitacionsDisponibles = habitacioService.obtenirHabitacionsDisponibles();

        if (habitacionsDisponibles.isEmpty()) {
            System.out.println("No hi ha cap habitació disponible.");
            return;
        }

        System.out.println("Habitacions disponibles:");
        for (Habitacio habitacio : habitacionsDisponibles) {
            System.out.println(habitacio);
        }

        System.out.print("Número d'habitació a reservar: ");
        int numeroHabitacio = llegirOpcio();

        // Mostrar els clients
        List<Client> clients = clientService.obtenirTotsElsClients();

        if (clients.isEmpty()) {
            System.out.println("No hi ha cap client registrat. Cal afegir un client primer.");
            return;
        }

        System.out.println("Clients registrats:");
        for (Client client : clients) {
            System.out.println(client);
        }

        System.out.print("ID del client: ");
        int idClient = llegirOpcio();

        System.out.print("Data d'entrada (dd/mm/aaaa): ");
        LocalDate dataEntrada = llegirData();

        System.out.print("Data de sortida (dd/mm/aaaa): ");
        LocalDate dataSortida = llegirData();

        int idReserva = reservaService.realitzarReserva(numeroHabitacio, idClient, dataEntrada, dataSortida);

        if (idReserva > 0) {
            System.out.println("Reserva realitzada correctament amb ID: " + idReserva);
        } else {
            System.out.println("No s'ha pogut realitzar la reserva.");
        }
    }

    /**
     * Cancel·la una reserva.
     */
    private static void cancelarReserva() {
        System.out.println("\n--- Cancel·lar una reserva ---");

        System.out.print("ID de la reserva a cancel·lar: ");
        int idReserva = llegirOpcio();

        boolean resultat = reservaService.cancelarReserva(idReserva);

        if (resultat) {
            System.out.println("Reserva cancel·lada correctament.");
        } else {
            System.out.println("No s'ha pogut cancel·lar la reserva.");
        }
    }

    /**
     * Consulta totes les reserves actives.
     */
    private static void consultarReservesActives() {
        System.out.println("\n--- Reserves actives ---");

        List<Reserva> reserves = reservaService.obtenirReservesActives();

        if (reserves.isEmpty()) {
            System.out.println("No hi ha cap reserva activa.");
        } else {
            for (Reserva reserva : reserves) {
                System.out.println(reserva);
            }
        }
    }

    /**
     * Consulta les reserves d'un client.
     */
    private static void consultarReservesClient() {
        System.out.println("\n--- Reserves d'un client ---");

        System.out.print("ID del client: ");
        int idClient = llegirOpcio();

        List<Reserva> reserves = reservaService.obtenirReservesClient(idClient);

        if (reserves.isEmpty()) {
            System.out.println("No hi ha cap reserva per a aquest client.");
        } else {
            for (Reserva reserva : reserves) {
                System.out.println(reserva);
            }
        }
    }

    // Mètodes d'utilitat

    /**
     * Llegeix un valor double del teclat.
     * @return El valor double.
     */
    private static double llegirDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor no vàlid. S'utilitzarà 0.0.");
            return 0.0;
        }
    }

    /**
     * Llegeix una data del teclat.
     * @return La data.
     */
    private static LocalDate llegirData() {
        while (true) {
            try {
                String dataStr = scanner.nextLine();
                return LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.print("Format de data no vàlid. Torna a introduir-la (dd/mm/aaaa): ");
            }
        }
    }

    /**
     * Llegeix una data d'una cadena.
     * @param dataStr La cadena amb la data.
     * @return La data.
     */
    private static LocalDate llegirData(String dataStr) {
        try {
            return LocalDate.parse(dataStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Format de data no vàlid. S'utilitzarà la data actual.");
            return LocalDate.now();
        }
    }
}