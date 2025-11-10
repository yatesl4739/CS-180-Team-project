import java.util.ArrayList;

/**
 * UserDatabaseInterface
 * <p>
 * This program is an interface for UserDatabase.
 *
 * @author Chelsea Zhao, lab section L12
 * @version November 10, 2025
 *
 */

public interface UserDataBaseInterface {

    /**
     * Sets list of Users
     *
     * @param given list of Users
     */
    public void setUsers(ArrayList<User> users);

    /**
     * Returns list of Users
     *
     * @return list of Users
     */
    public ArrayList<User> getUserDb();

    /**
     * Adds a user to the list
     *
     * @param given User object
     */
    public void addUser(User u);

    /**
     * Removes a user from the list by object, returns true if successful
     *
     * @param given User object
     * @return boolean representing success
     */
    public boolean removeUser(User u);

    /**
     * Removes a user from the list by index, returns true if successful
     *
     * @param index of user
     * @return boolean representing success
     */
    public boolean removeUser(int index);

}