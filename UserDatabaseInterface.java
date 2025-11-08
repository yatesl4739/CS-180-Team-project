import java.util.ArrayList;

public interface UserDataBaseInterface {

    private ArrayList<User> userDb;

    public void setUsers(ArrayList<User> users);

    public ArrayList<User> getUserDb();

    public void addUser(User u);

    public boolean removeUser(User u);

    public boolean removeUser(int index);

}