import java.io.Serializable;

/**
 * User
 *
 * This program represents a User that can
 * make reservations.
 *
 * @author Chelsea Zhao, lab section L12
 *
 * @version November 10, 2025
 *
 */

public class User implements UserInterface, Serializable {

    private String username;  // User's username
    private String password;  // User's password
    private ReservationDatabase reservationDb; // List of reservations the user has made
    private double cost; // Cost of all reservations

    /**
     * Constructor given a username and password,
     * creates empty list of reservations and sets cost to 0 as a new user
     * 
     * @param username of User
     * @param password of User
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        reservationDb = new ReservationDatabase();
        cost = 0.0;
    }

    /**
     * Sets username of user
     *
     * @param username of User
     */
    public synchronized void setUsername(String username){
        this.username = username;
    }

    /**
     * Sets password of user
     *
     * @param password of User
     */
    public synchronized void setPassword(String password){
        this.password = password;
    }

    /**
     * Sets list of user's reservations
     *
     * @param reservationDb list of user's reservations
     */
    public synchronized void setReservationDb(ReservationDatabase reservationDb) {
        this.reservationDb = reservationDb;
        for (Reservation r : reservationDb.getReservations()) {
            cost += r.getPrice();
        }
    }

    /**
     * Returns username of user
     *
     * @return username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Returns password of user
     *
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Returns list of user's reservations
     *
     * @return reservations
     */
    public ReservationDatabase getReservationDb() {
        return reservationDb;
    }
    
    /**
     * Returns total cost of reservations
     *
     * @return cost
     */
    public double getCost() { return cost; }
    
    /**
     * Adds a reservation to the list
     *
     * @param r object
     */
    public synchronized void addReservation(Reservation r) {
        reservationDb.addReservation(r);
        cost += r.getPrice();
    }

    /**
     * Removes a reservation based on index in the list
     *
     * @param index of reservation to remove
     */
    public synchronized void removeReservation(int index) {
        cost -= reservationDb.getReservations().get(index).getPrice();
        reservationDb.removeReservation(index);
    }
}