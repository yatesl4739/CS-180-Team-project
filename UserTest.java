import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void userOne() {
        User one = new User("mary", "verySecure");

        // Testing constructor, getters, setters
        assertEquals("mary", one.getUsername());
        assertEquals("verySecure", one.getPassword());
        assertEquals(0.0, one.getCost(), 0.0001);
        ReservationDatabase rdb = new ReservationDatabase();
        one.setReservationDb(rdb);
        assertEquals(rdb, one.getReservationDb());
        one.setPassword("moreSecure");
        one.setUsername("maryJane");
        assertEquals("maryJane", one.getUsername());
        assertEquals("moreSecure", one.getPassword());

        // Testing adding and removing reservations from reservationDb
        // Event and reservation arguments arbitrary other than price
        Event e = new Event("party", 5.00, new char[2][2], 1200, 10);
        Reservation r = new Reservation(e, one, 5, 1200, 10, 5.00, new int[2], new int[2]);
        one.addReservation(r);
        assertEquals(5.00, one.getCost(), 0.0001);
        one.removeReservation(0);
        assertEquals(0, one.getCost(), 0.0001);
    }
}
