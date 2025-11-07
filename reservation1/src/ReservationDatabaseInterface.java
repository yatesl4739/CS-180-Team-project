import java.util.ArrayList;

public interface ReservationDatabaseInterface {

    public ArrayList<Reservation> getReservations(); //return all reservations
    public void setReservations(ArrayList<Reservation> listInput); //replace reservation list
    public void addReservation(Reservation reservation); //add new reservation
    public void removeReservation(int index); //remove by index
    public ArrayList<Reservation> getUserReservations(String username); //filter by user
}
