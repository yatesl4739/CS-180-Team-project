/**
 * UserInterface
 *
 * This program is an interface for a User object.
 *
 * @author Chelsea Zhao, lab section L12
 *
 * @version November 10, 2025
 *
 */

public interface UserInterface {

    /**
     * Sets username of user
     *
     * @param username of User
     */
    public void setUsername(String username);

    /**
     * Sets password of user
     *
     * @param password of User
     */
    public void setPassword(String password);

    /**
     * Sets list of user's reservations
     *
     * @param reservationDb list of user's reservation
     */
    public void setReservationDb(ReservationDatabase reservationDb);

    /**
     * Returns username of user
     *
     * @return username
     */
    public String getUsername();

    /**
     * Returns password of user
     *
     * @return password
     */
    public String getPassword();

    /**
     * Returns total cost of reservations
     *
     * @return cost
     */
    public double getCost();

    /**
     * Returns list of user's reservations
     *
     * @return reservations
     */
    public ReservationDatabase getReservationDb();

    /**
     * Adds a reservation to the list
     *
     * @param r reservation object
     */
    public void addReservation(Reservation r);

    /**
     * Removes a reservation based on index in the list
     *
     * @param index of reservation
     */
    public void removeReservation(int index);

}