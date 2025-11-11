import java.util.ArrayList;
import java.io.*;

/**
 * UserDatabaseInterface
 * <p>
 * This program is an interface for UserDatabase.
 *
 * @author Chelsea Zhao, lab section L12
 * @version November 10, 2025
 *
 */

public interface UserDatabaseInterface {

    /**
     * Sets list of Users
     *
     * @param users list of Users
     */
    public void setUsers(ArrayList<User> users);

    /**
     * Returns list of Users
     *
     * @return list of Users
     */
    public ArrayList<User> getUserDb();

    /**
     * Returns User at given index
     *
     * @param index of User
     * @return user at index
     */
    public User get(int index);

    /**
     * Adds a user to the list
     *
     * @param u User object
     */
    public void addUser(User u);

    /**
     * Removes a user from the list by object, returns true if successful
     *
     * @param u User object
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

    /**
     * Updates the save file with the list of Users
     */
    public void updateSaveFile();

}