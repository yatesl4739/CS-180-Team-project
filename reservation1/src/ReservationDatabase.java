import java.util.ArrayList;

public class ReservationDatabase implements ReservationDatabaseInterface{
    private ArrayList<Reservation> reservationList;

    // constructors
    public ReservationDatabase() {
        this.reservationList = new ArrayList<Reservation>();
    }

    public ReservationDatabase(ArrayList<Reservation> reservations) {
        this.reservationList = reservations;
    }

    // interface methods
    public ArrayList<Reservation> getReservations() {
        return reservationList;
    }

    public void setReservations(ArrayList<Reservation> listInput) {
        this.reservationList = listInput;
    }

    public void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }

    public void removeReservation(int index) {
        this.reservationList.remove(index);
    }

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
