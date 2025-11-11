import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ReservationDatabaseTest {

    public User makeUser(String name) {
        return new User(name, "pw");
    }

    public Event makeEvent() {
        return new Event("TestEvent", 10.0, new char[][]{{'x'}}, 1L, 1L);
    }

     private Reservation makeReservation(String username) {
        return new Reservation(makeEvent(),
                makeUser(username),
                1, 10L, 20L, 10.0,
                new int[]{0}, new int[]{0});
     }

     public void testDefaultConstructor() {
        ReservationDatabase db = new ReservationDatabase();
        assertEquals("Reservation list should start empty", 0, db.getReservations().size());
     }

    @Test
    public void testConstructor() {
        ArrayList<Reservation> list = new ArrayList<>();
        list.add(makeReservation("bob"));

        ReservationDatabase db = new ReservationDatabase(list);

        assertEquals("List passed to constructor should be used",
                1, db.getReservations().size());
        assertEquals("bob", db.getReservations().get(0).getUser().getUsername());
    }

     @Test
     public void testGetterSetter() {
         ReservationDatabase db = new ReservationDatabase();

         ArrayList<Reservation> newList = new ArrayList<>();
         newList.add(makeReservation("mike"));

         db.setReservations(newList);

         assertEquals("Setter should replace reservation list",
                 1, db.getReservations().size());
         assertEquals("mike", db.getReservations().get(0).getUser().getUsername());
     }
     @Test
     public void testAddReservation() {
         ReservationDatabase db = new ReservationDatabase();

         Reservation r = makeReservation("chris");
         db.addReservation(r);

         assertEquals(1, db.getReservations().size());
         assertEquals("chris", db.getReservations().get(0).getUser().getUsername());
     }

     @Test
     public void testRemoveReservation() {
         ReservationDatabase db = new ReservationDatabase();

         db.addReservation(makeReservation("x"));
         db.addReservation(makeReservation("y"));

         db.removeReservation(0);

         assertEquals("Size should be 1 after removal", 1, db.getReservations().size());
         assertEquals("Remaining reservation should belong to y",
                 "y", db.getReservations().get(0).getUser().getUsername());

     }

     @Test
     public void testGetUserReservations() {
         ReservationDatabase db = new ReservationDatabase();
         db.addReservation(makeReservation("bobby"));
         db.addReservation(makeReservation("bob"));
         db.addReservation(makeReservation("bobby"));

         ArrayList<Reservation> results = db.getUserReservations("bobby");
         assertEquals(2, results.size());
         for (Reservation r : results) {
             assertEquals("bobby", r.getUser().getUsername());
         }
     }

}