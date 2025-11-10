import java.util.ArrayList;
import java.io.*;

/**
 * ReservationDatabase
 *
 * @author Meraj Syeda
 *
 * @version 11/10/25
 *
 * a threadsafe database that manages all reservations made and implements ReservationDatabaseInterface
 * supports adding and removing reservations as well as saving/loading from a file
 */

public class ReservationDatabase implements ReservationDatabaseInterface, Serializable {

    private static final long serialVersionUID = 1L;

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
    public synchronized ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservationList);
    }

    public synchronized void setReservations(ArrayList<Reservation> listInput) {
        this.reservationList = listInput;
    }

    /**
     * passed a reservation object that is created and
     * adds that to the list of existing reservations
     * @param reservation
     */
    public synchronized void addReservation(Reservation reservation) {
        this.reservationList.add(reservation);
    }

    /**
     * passed an index and removes the reservation that was at that index
     * from the list of existing reservations
     * @param index
     */
    public synchronized void removeReservation(int index) {
        if (index >= 0 && index < reservationList.size()) {
            this.reservationList.remove(index);
        }
    }

    /**
     * given a specific username, creates an arraylist which is a list of reservations
     * made under that username and adds the new reservation made to that list
     * @param username
     * @return the reservations under that specific user
     */
    public synchronized ArrayList<Reservation> getUserReservations(String username) {
        ArrayList<Reservation> userReservations = new ArrayList<>();
        for (Reservation res : reservationList) {
            if (res.getUser().equals(username)) {
                userReservations.add(res);
            }
        }
        return userReservations;
    }

    // file i/o methods

    /**
     * saves the current database to a file
     * @param filename the name of the file to save to
     */
    public synchronized void saveToFile(String filename) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(reservationList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the database from a file
     * @param filename the name of the file to load from
     */
    public synchronized void loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            this.reservationList = (ArrayList<Reservation>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing database found. Starting fresh.");
            this.reservationList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.reservationList = new ArrayList<>();
        }
    }


}
