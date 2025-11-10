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
     * @param username
     */
    public void setUsername(String username);

    /**
     * Sets password of user
     *
     * @param password
     */
    public void setPassword(String password);

    /**
     * Sets list of user's reservations
     *
     * @param reservationDb
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
     * Returns list of user's reservations
     *
     * @return reservations
     */
    public ReservationDatabase getReservationDb();

    /**
     * Adds a reservation to the list
     *
     * @param Reservation object
     */
    public void addReservation(Reservation r);

    /**
     * Removes a reservation based on index in the list
     *
     * @param index
     */
    public void removeReservation(int index);

}