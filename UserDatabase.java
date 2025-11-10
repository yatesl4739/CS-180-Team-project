import java.util.ArrayList;

/**
 * UserDatabase
 * <p>
 * This program stores an ArrayList of Users.
 *
 * @author Chelsea Zhao, lab section L12
 * @version November 10, 2025
 *
 */

public class UserDatabase implements UserDatbaseInterface {

    private ArrayList<User> userDb;  // list of users
    Object lock = new Object();  // lock for synchronization

    /**
     * Constructor given no parameters, creates empty list
     */
    public UserDatabase() {
        ArrayList<User> userDb = new ArrayList<User>();
    }

    /**
     * Constructor given a list of Users, sets list to given list
     *
     * @param list of Users given
     */
    public UserDatabase(ArrayList<User> userDb) {
        this.userDb = userDb;
    }

    /**
     * Constructor given a single User, creates list with User added
     *
     * @param single given User
     */
    public UserDatabase(User u) {
        ArrayList<User> userDb = new ArrayList<User>();
        userDb.add(u);
    }

    /**
     * Sets list of Users
     *
     * @param given list of Users
     */
    public void setUsers(ArrayList<User> users) {
        synchronized (lock) {
            userDb = users;
        }
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
     * Adds a user to the list
     *
     * @param given User object
     */
    public void addUser(User u) {
        synchronized (lock) {
            userDb.add(u);
        }
    }

    /**
     * Removes a user from the list by object, returns true if successful
     *
     * @param given User object
     * @return boolean representing success
     */
    public boolean removeUser(User u) {
        synchronized (lock) {
            return userDb.remove(u);
        }
    }

    /**
     * Removes a user from the list by index, returns true if successful
     *
     * @param index of user
     * @return boolean representing success
     */
    public boolean removeUser(int index) {
        synchronized (lock) {
            try {
                userDb.remove(index);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
    }

}