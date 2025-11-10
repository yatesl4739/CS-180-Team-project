import java.util.ArrayList;

/**
 * ReservationDatabase
 *
 * @author Meraj Syeda
 *
 * @version 11/10/25
 */

/**
 * database that manages all reservations made and implements ReservationDatabaseInterface
 * can be used to add and remove reservations
 */

public class ReservationDatabase implements ReservationDatabaseInterface{
    private ArrayList<Reservation> reservationList; // an arraylist of all existing reservations

    // constructor, passed nothing
    public ReservationDatabase() {
        this.reservationList = new ArrayList<Reservation>();
    }

    // pass list of reservations
    public ReservationDatabase(ArrayList<Reservation> reservations) {
        this.reservationList = reservations;
    }

    // getter and setter
    public ArrayList<Reservation> getReservations() {
        return reservationList;
    }

    public void setReservations(ArrayList<Reservation> listInput) {
        this.reservationList = listInput;
    }

    /**
     * passed a reservation object that is created and
     * adds that to the list of existing reservations
     * @param reservation
     */
    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }

    /**
     * passed an index and removes the reservation that was at that index
     * from the list of existing reservations
     * @param index
     */
    public void removeReservation(int index) {
        this.reservationList.remove(index);
    }

    /**
     * given a specific username, creates an arraylist which is a list of reservations
     * made under that username and adds the new reservation made to that list
     * @param username
     * @return the reservations under that specific user
     */
    public ArrayList<Reservation> getUserReservations(String username) {
        ArrayList<Reservation> userReservations = new ArrayList<>();
        for (Reservation res : reservationList) {
            if (res.getUser().equals(username)) {
                userReservations.add(res);
            }
        }
        return userReservations;
    }
}
