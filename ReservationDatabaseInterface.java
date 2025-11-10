import java.util.ArrayList;

public interface ReservationDatabaseInterface {

    /**
     * method to get list of reservations
     * @return all existing reservations
     */
    public ArrayList<Reservation> getReservations();

    /**
     * method to replace reservation list
     * @param listInput
     */
    public void setReservations(ArrayList<Reservation> listInput);

    /**
     * method to add new reservation
     * @param reservation
     */
    public void addReservation(Reservation reservation);

    /**
     * method to remove reservations from reservation list by index
     * @param index
     */
    public void removeReservation(int index);

    /**
     * method to filter reservations by user
     * @param username
     * @return
     */
    public ArrayList<Reservation> getUserReservations(String username);
}
