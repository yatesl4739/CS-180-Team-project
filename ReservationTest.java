import org.junit.Test;
import static org.junit.Assert.*;

public class ReservationTest {
    public void testReservationConstructorAndGetters() {
        // setup
        User alice = new User("bob", "pw");
        Event event = new Event("Movie", 10.0, new char[][]{{'o','o'}}, 100L, 200L);
        int[] x = {0, 1};
        int[] y = {0, 1};
        Reservation r = new Reservation(event, alice, 2, 100L, 200L, 50.0, x, y);

        // assertions
        assertEquals("User incorrect", "bob", r.getUser().getUsername());
        assertEquals(2, r.getNumPeople());
        assertEquals(100L, r.getTimeOfReservation());
        assertEquals(200L, r.getDate());
        assertEquals(50.0, r.getPrice(), 0.001);
        assertArrayEquals(x, r.getX());
        assertArrayEquals(y, r.getY());
        assertEquals(event, r.getEvent());

        // check reservedSeats 2D array
        int[][] expectedSeats = {{0,1},{0,1}};
        assertArrayEquals(expectedSeats, r.getReservedSeats());
    }

    @Test
    public void testSetters() {
        User bob = new User("bob", "pw");
        Event event1 = new Event("Show", 5.0, new char[][]{{'o'}}, 50L, 60L);
        int[] x1 = {0};
        int[] y1 = {0};
        Reservation r = new Reservation(event1, bob, 1, 50L, 60L, 20.0, x1, y1);

        // change values
        User charlie = new User("charlie", "pw");
        Event event2 = new Event("Concert", 15.0, new char[][]{{'o','o'}}, 200L, 300L);
        int[] newX = {0,1};
        int[] newY = {1,0};

        r.setUser(charlie);
        r.setNumPeople(2);
        r.setTimeOfReservation(100L);
        r.setDate(200L);
        r.setPrice(50.0);
        r.setEvent(event2);
        r.setXY(newX, newY);

        // assertions
        assertEquals("User setter failed", "charlie", r.getUser().getUsername());
        assertEquals(2, r.getNumPeople());
        assertEquals(100L, r.getTimeOfReservation());
        assertEquals(200L, r.getDate());
        assertEquals(50.0, r.getPrice(), 0.001);
        assertEquals(event2, r.getEvent());
        assertArrayEquals(newX, r.getX());
        assertArrayEquals(newY, r.getY());

        int[][] expectedSeats = {{0,1},{1,0}};
        assertArrayEquals(expectedSeats, r.getReservedSeats());
    }
}