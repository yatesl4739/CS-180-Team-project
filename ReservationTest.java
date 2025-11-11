import org.junit.Test;
import static org.junit.Assert.*;

public class ReservationTest {

    /**
     * tests the constructor and getters for the Reservation class
     */
    @Test
    public void testConstructorsandGetters() {
        // setup
        User user = new User("bob", "pw");
        Event event = new Event("Movie", 10.0, new char[][]{{'o','o'}}, 100L, 200L);
        int[] x = {1, 2};
        int[] y = {3, 4};
        Reservation r = new Reservation(event, user, 2, 100L, 200L, 50.0, x, y);

        assertEquals("User incorrect", "bob", r.getUser().getUsername());
        assertEquals("Event incorrect", event, r.getEvent());
        assertEquals("Num people incorrect",
                2, r.getNumPeople());
        assertEquals("Time incorrect",
                100L, r.getTimeOfReservation());
        assertEquals("Date incorrect",
                200L, r.getDate());
        assertEquals("Price incorrect",
                50.0, r.getPrice(), 0.001);

        // check reservedSeats 2D array
        int[][] seats = r.getReservedSeats();
        assertEquals("X seat incorrect", 2, seats[0].length);
        assertEquals("Y seat incorrect", 2, seats[1].length);

        assertEquals(1, seats[0][0]);
        assertEquals(2, seats[0][1]);
        assertEquals(3, seats[1][0]);
        assertEquals(4, seats[1][1]);

        assertEquals("getX incorrect", x, r.getX());
        assertEquals("getY incorrect", y, r.getY());
    }

    @Test
    public void testSetters() {
        User u1 = new User("bob", "pw");
        Event e1 = new Event("Show", 5.0, new char[][]{{'o'}}, 50L, 60L);
        int[] x1 = {0};
        int[] y1 = {0};
        Reservation r1 = new Reservation(e1, u1, 1, 50L, 60L, 20.0, x1, y1);

        // change values
        User u2 = new User("charlie", "pass");
        Event e2 = new Event("Concert", 15.0, new char[][]{{'x'}}, 200L, 300L);
        int[] newX = {1};
        int[] newY = {2};

        r1.setUser(u2);
        r1.setEvent(e2);
        r1.setNumPeople(2);
        r1.setTimeOfReservation(100L);
        r1.setDate(200L);
        r1.setPrice(50.0);
        r1.setXY(newX, newY);

        // assertions
        assertEquals("User setter failed", "charlie", r1.getUser().getUsername());
        assertEquals("Event setter failed", e2, r1.getEvent());
        assertEquals("NumPeople setter failed", 2, r1.getNumPeople());
        assertEquals("Time setter failed", 100L, r1.getTimeOfReservation());
        assertEquals("Date setter failed", 200L, r1.getDate());
        assertEquals("Price setter failed", 50.0, r1.getPrice(), 0.001);

        assertEquals(1, r1.getReservedSeats()[0][0]);
        assertEquals(2, r1.getReservedSeats()[1][0]);

        int[] newY2 = {5};
        r1.setY(newY2);

        assertArrayEquals("setY incorrect", newY2, r1.getY());
    }

    @Test
    public void testToString() {
        User u = new User("bob", "pw");
        Event e = new Event("Concert", 20.0,
                new char[][]{{'x'}}, 100L, 2L);

        Reservation r = new Reservation(
                e, u, 1, 100L, 2L, 20.0,
                new int[]{1}, new int[]{2}
        );

        String result = r.toString();

        assertTrue("toString should contain username",
                result.contains("bob"));
        assertTrue("toString should contain price",
                result.contains("20.0"));
    }
}