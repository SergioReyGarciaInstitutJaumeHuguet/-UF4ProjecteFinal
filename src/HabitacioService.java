import java.util.List;

/**
 * Servei que gestiona les operacions de negoci relacionades amb les habitacions.
 */
public class HabitacioService {

    private HabitacioDAO habitacioDAO;

    /**
     * Constructor que inicialitza el DAO.
     */
    public HabitacioService() {
        this.habitacioDAO = new HabitacioDAO();
    }

    /**
     * Afegeix una nova habitació.
     * @param numeroHabitacio El número de l'habitació.
     * @param tipus El tipus d'habitació (individual, doble, suite, etc.).
     * @param preuPerNit El preu per nit.
     * @return true si s'ha afegit correctament, false en cas contrari.
     */
    public boolean afegirHabitacio(int numeroHabitacio, String tipus, double preuPerNit) {
        // Verificar que el número de l'habitació no existeixi
        if (habitacioDAO.obtenirHabitacio(numeroHabitacio) != null) {
            System.err.println("Error: Ja existeix una habitació amb el número " + numeroHabitacio);
            return false;
        }

        // Verificar que el preu per nit sigui positiu
        if (preuPerNit <= 0) {
            System.err.println("Error: El preu per nit ha de ser positiu");
            return false;
        }

        Habitacio habitacio = new Habitacio(numeroHabitacio, tipus, preuPerNit, true);
        return habitacioDAO.afegirHabitacio(habitacio);
    }

    /**
     * Elimina una habitació.
     * @param numeroHabitacio El número de l'habitació a eliminar.
     * @return true si s'ha eliminat correctament, false en cas contrari.
     */
    public boolean eliminarHabitacio(int numeroHabitacio) {
        // Verificar que l'habitació existeixi
        if (habitacioDAO.obtenirHabitacio(numeroHabitacio) == null) {
            System.err.println("Error: No existeix cap habitació amb el número " + numeroHabitacio);
            return false;
        }

        return habitacioDAO.eliminarHabitacio(numeroHabitacio);
    }

    /**
     * Actualitza la informació d'una habitació.
     * @param numeroHabitacio El número de l'habitació.
     * @param tipus El nou tipus d'habitació.
     * @param preuPerNit El nou preu per nit.
     * @param disponible La nova disponibilitat.
     * @return true si s'ha actualitzat correctament, false en cas contrari.
     */
    public boolean actualitzarHabitacio(int numeroHabitacio, String tipus, double preuPerNit, boolean disponible) {
        // Verificar que l'habitació existeixi
        Habitacio habitacio = habitacioDAO.obtenirHabitacio(numeroHabitacio);
        if (habitacio == null) {
            System.err.println("Error: No existeix cap habitació amb el número " + numeroHabitacio);
            return false;
        }

        // Verificar que el preu per nit sigui positiu
        if (preuPerNit <= 0) {
            System.err.println("Error: El preu per nit ha de ser positiu");
            return false;
        }

        habitacio.setTipus(tipus);
        habitacio.setPreuPerNit(preuPerNit);
        habitacio.setDisponible(disponible);

        return habitacioDAO.actualitzarHabitacio(habitacio);
    }

    /**
     * Obté una habitació pel seu número.
     * @param numeroHabitacio El número de l'habitació.
     * @return L'habitació o null si no s'ha trobat.
     */
    public Habitacio obtenirHabitacio(int numeroHabitacio) {
        return habitacioDAO.obtenirHabitacio(numeroHabitacio);
    }

    /**
     * Obté totes les habitacions.
     * @return Una llista amb totes les habitacions.
     */
    public List<Habitacio> obtenirTotesLesHabitacions() {
        return habitacioDAO.obtenirTotesLesHabitacions();
    }

    /**
     * Obté totes les habitacions disponibles.
     * @return Una llista amb totes les habitacions disponibles.
     */
    public List<Habitacio> obtenirHabitacionsDisponibles() {
        return habitacioDAO.obtenirHabitacionsDisponibles();
    }
}