public class User implements UserInterface {

    private String username;
    private String password;
    private ReservationDatabase reservationDb;

    // Constructors

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        reservationDb = new ReservationDatabase();
    }

    // Get/Set

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setReservationDb(ReservationDatabase reservationDb) {
        this.reservationDb = reservationDb;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public ReservationDatabase getReservationDb() {
        return reservationDb;
    }

    // Add Reservation
    public void addReservation(Reservation r) {
        reservationDb.addReservation(r);
    }

    // Remove Reservation by index
    public void removeReservation(int index) {
        reservationDb.removeReservation(index);
    }
}