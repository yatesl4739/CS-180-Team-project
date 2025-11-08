import java.util.ArrayList;

public class UserDatabase implements UserDatbaseInterface {

    private ArrayList<User> userDb = new ArrayList<User>();

    // Constructors

    public UserDatabase() { }

    public UserDatabase(ArrayList<User> userDb) {
        this.userDb = userDb;
    }

    public UserDatabase(User u) {
        userDb.add(u);
    }

    // Get/Set

    public void setUsers(ArrayList<User> users) {
        userDb = users;
    }

    public ArrayList<User> getUserDb() {
        return userDb;
    }

    // Add User
    public void addUser(User u) {
        userDb.add(u);
    }

    // Remove User by object, return true if successful
    public boolean removeUser(User u) {
        return userDb.remove(u);
    }

    // Remove User by index, return true if succesful
    public boolean removeUser(int index) {
        return userDb.remove(index);
    }

}