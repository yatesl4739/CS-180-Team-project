import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserDatabaseTest {
    @Test
    public void dbOne() {
        // testing constructor (no arguments)
        UserDatabase one = new UserDatabase();
        assertEquals(0, one.getUserDb().size());
        // testing changing variables
        ArrayList<User> list = new ArrayList<>();
        User jane = new User("jane", "password");
        list.add(jane);
        one.setUsers(list);
        assertEquals(list, one.getUserDb());
        assertEquals(jane, one.get(0));
        User bob = new User("bob", "otherPassword");
        one.addUser(bob);
        assertEquals(bob, one.get(1));
        assertEquals(true, one.removeUser(bob));
        assertEquals(false, one.removeUser(bob));
        one.addUser(bob);
        assertEquals(true, one.removeUser(1));
        assertEquals(false, one.removeUser(1));
    }

    @Test
    public void dbTwo() {
        // testing constructor (single user argument)
        User jane = new User("jane", "pw");
        UserDatabase two = new UserDatabase(jane);
        assertEquals(jane, two.get(0));
    }

    @Test
    public void dbThree() {
        // testing constructor (User arraylist argument)
        ArrayList<User> list = new ArrayList<>();
        User jane = new User("jane", "password");
        list.add(jane);
        UserDatabase three = new UserDatabase(list);
        assertSame(list, three.getUserDb());
    }
}