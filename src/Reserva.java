import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * JavaBean que representa una reserva de l'hotel.
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idReserva;
    private Habitacio habitacio;
    private Client client;
    private LocalDate dataEntrada;
    private LocalDate dataSortida;
    private double totalAPagar;

    /**
     * Constructor per defecte (requerit per JavaBean)
     */
    public Reserva() {
    }

    /**
     * Constructor amb tots els paràmetres excepte totalAPagar (es calcula automàticament)
     */
    public Reserva(int idReserva, Habitacio habitacio, Client client, LocalDate dataEntrada, LocalDate dataSortida) {
        this.idReserva = idReserva;
        this.habitacio = habitacio;
        this.client = client;
        this.dataEntrada = dataEntrada;
        this.dataSortida = dataSortida;
        calcularTotalAPagar();
    }

    /**
     * Calcula el total a pagar en funció dels dies d'estada i el preu per nit de l'habitació
     */
    private void calcularTotalAPagar() {
        if (dataEntrada != null && dataSortida != null && habitacio != null) {
            long dies = ChronoUnit.DAYS.between(dataEntrada, dataSortida);
            this.totalAPagar = dies * habitacio.getPreuPerNit();
        } else {
            this.totalAPagar = 0.0;
        }
    }

    // Getters i Setters (requerits per JavaBean)

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Habitacio getHabitacio() {
        return habitacio;
    }

    public void setHabitacio(Habitacio habitacio) {
        this.habitacio = habitacio;
        calcularTotalAPagar(); // Recalcular si es canvia l'habitació
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
        calcularTotalAPagar(); // Recalcular si es canvia la data d'entrada
    }

    public LocalDate getDataSortida() {
        return dataSortida;
    }

    public void setDataSortida(LocalDate dataSortida) {
        this.dataSortida = dataSortida;
        calcularTotalAPagar(); // Recalcular si es canvia la data de sortida
    }

    public double getTotalAPagar() {
        return totalAPagar;
    }

    // No s'hauria de poder modificar directament el total a pagar, ja que es calcula
    private void setTotalAPagar(double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    @Override
    public String toString() {
        return "Reserva [ID: " + idReserva +
                ", Habitació: " + habitacio.getNumeroHabitacio() +
                ", Client: " + client.getNom() + " " + client.getCognoms() +
                ", Data Entrada: " + dataEntrada +
                ", Data Sortida: " + dataSortida +
                ", Total a Pagar: " + totalAPagar + "€]";
    }
}