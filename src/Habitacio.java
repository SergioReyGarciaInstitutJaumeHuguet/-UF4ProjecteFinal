import java.io.Serializable;

/**
 * JavaBean que representa una habitació de l'hotel.
 */
public class Habitacio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numeroHabitacio;
    private String tipus;
    private double preuPerNit;
    private boolean disponible;

    /**
     * Constructor per defecte (requerit per JavaBean)
     */
    public Habitacio() {
    }

    /**
     * Constructor amb tots els paràmetres
     */
    public Habitacio(int numeroHabitacio, String tipus, double preuPerNit, boolean disponible) {
        this.numeroHabitacio = numeroHabitacio;
        this.tipus = tipus;
        this.preuPerNit = preuPerNit;
        this.disponible = disponible;
    }

    // Getters i Setters (requerits per JavaBean)

    public int getNumeroHabitacio() {
        return numeroHabitacio;
    }

    public void setNumeroHabitacio(int numeroHabitacio) {
        this.numeroHabitacio = numeroHabitacio;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public double getPreuPerNit() {
        return preuPerNit;
    }

    public void setPreuPerNit(double preuPerNit) {
        this.preuPerNit = preuPerNit;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Habitació [Número: " + numeroHabitacio +
                ", Tipus: " + tipus +
                ", Preu per nit: " + preuPerNit +
                ", Disponible: " + (disponible ? "Sí" : "No") + "]";
    }
}