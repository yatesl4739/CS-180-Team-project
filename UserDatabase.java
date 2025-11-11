import java.util.ArrayList;
import java.io.*;

/**
 * UserDatabase
 * <p>
 * This program stores an ArrayList of Users.
 *
 * @author Chelsea Zhao, lab section L12
 * @version November 10, 2025
 *
 */

public class UserDatabase implements UserDatabaseInterface {

    private ArrayList<User> userDb;  // list of users
    private final File SAVE_FILE = new File("/saveFiles/userDatabase.file");  // save file to write list of Users

    /**
     * Constructor given no parameters, creates empty list
     */
    public UserDatabase() {
        ArrayList<User> userDb = new ArrayList<>();
        updateSaveFile();
    }

    /**
     * Constructor given a list of Users, sets list to given list
     *
     * @param userDb list of Users given
     */
    public UserDatabase(ArrayList<User> userDb) {
        this.userDb = userDb;
        updateSaveFile();
    }

    /**
     * Constructor given a single User, creates list with User added
     *
     * @param u given User
     */
    public UserDatabase(User u) {
        ArrayList<User> userDb = new ArrayList<>();
        userDb.add(u);
        updateSaveFile();
    }

    /**
     * Sets list of Users
     *
     * @param users list of Users
     */
    public synchronized void setUsers(ArrayList<User> users) {
        userDb = users;
        updateSaveFile();
    }

    /**
     * Returns list of Users
     *
     * @return list of Users
     */
    public ArrayList<User> getUserDb() {
        return userDb;
    }

    /**
     * Returns User at given index
     *
     * @param index of User
     * @return user at index
     */
    public User get(int index) {
        return userDb.get(index);
    }

    /**
     * Adds a user to the list
     *
     * @param u User object
     */
    public synchronized void addUser(User u) {
        userDb.add(u);
        updateSaveFile();
    }

    /**
     * Removes a user from the list by object, returns true if successful
     *
     * @param u User object
     * @return boolean representing success
     */
    public synchronized boolean removeUser(User u) {
        boolean success = userDb.remove(u);
        updateSaveFile();
        return success;
    }

    /**
     * Removes a user from the list by index, returns true if successful
     *
     * @param index of user
     * @return boolean representing success
     */
    public synchronized boolean removeUser(int index) {
        try {
            userDb.remove(index);
            updateSaveFile();
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Updates the save file with the list of Users
     */
    public synchronized void updateSaveFile() {
        try (FileOutputStream fos = new FileOutputStream(SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}