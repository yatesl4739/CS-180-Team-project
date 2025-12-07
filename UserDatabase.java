import java.util.ArrayList;
import java.io.*;

/**
 * UserDatabase
 * <p>
 * This program stores an ArrayList of Users.
 *
 * @author Chelsea Zhao, lab section L12
 * @version November 10, 2025
 */

public class UserDatabase implements UserDatabaseInterface, Serializable {

    private ArrayList<User> userDb;  // list of users
    private final File SAVE_FILE = new File("saveFiles/userDatabase.file");  // save file to write list of Users

    /**
     * Constructor given no parameters, creates empty list
     */
    public UserDatabase() {
        if (SAVE_FILE.canRead() &&
                SAVE_FILE.exists() &&
                SAVE_FILE.length() != 0) {
            userDb = getObjectFromSaveFile().getUserDb();
        }
        else {
            userDb = new ArrayList<>();
            updateSaveFile();
        }
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
        userDb = new ArrayList<>();
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
     * get the user database from the save file and load it into the current object
     *
     * @return this UserDatabase
     */
    private synchronized UserDatabase getObjectFromSaveFile() {

        try {
            FileInputStream fis = new FileInputStream(SAVE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            UserDatabase fromSaveFile = null;
            try {
                fromSaveFile = (UserDatabase)ois.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("The save file is empty and or has incorrect save data");
            }
            if (fromSaveFile == null) {
                System.out.println("Error! null has been returned");
                return null;
            }
            return fromSaveFile;


        } catch (IOException e) {
            System.out.println("Error reading from the save file");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the save file with the list of Users
     */
    public synchronized void updateSaveFile() {
        if (SAVE_FILE.exists()) {
            System.out.println("UserDatabase file was found");
            try (FileOutputStream fos = new FileOutputStream(SAVE_FILE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                 oos.writeObject(this);
                 System.out.println("objects written sucsess");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            throw new RuntimeException("SAVE FILE for UserDatabase was not found");
        }
    }

    /**
     * login function to handle a login request from the server
     */
    public User login(String username, String password) {

        //check if username exists
        for (int i = 0; i < userDb.size(); i++) {
            if (username.equals(userDb.get(i).getUsername())) {

                //correct user has been found
                User correctUser = userDb.get(i);

                if (password.equals(correctUser.getPassword())) {
                    //SUCSESS LOGIN
                    return correctUser;
                } else {
                    //PASSWORD INCORRECT
                    return new User("PASSWORD INCORRECT", null);
                }
            }

        }
        return new User("USER NOT FOUND", null);
    }

    /**
     * handle a user signing up
     */
    public User signUp(String username, String password) {
        //check if username is against restricted usernames
        if (username.equals("PASSWORD INCORRECT") ||
                username.equals("USER NOT FOUND") ||
                username.equals("INVALID USERNAME") ||
                username.equals("USERNAME TAKEN") ||
                username.equals("PASSWORD INVALID")) {

            return new User("INVALID USERNAME", null);
        }

        //check to see if username is available
        for (int i = 0; i < userDb.size(); i++) {
            if (username.equals(userDb.get(i).getUsername())) {
                //username taken
                return new User("USERNAME TAKEN", null);
            }

        }
        //else: create a new user

        //check if there is a password provided

        if (!(password.length() > 0)) {
            return new User("PASSWORD INVALID", null);
        }



        User newUser = new User(username, password);
        this.addUser(newUser);
        return newUser;



    }

}