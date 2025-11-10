import org.junit.Test;
import static org.junit.Assert.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class EventTest {
    @Test
    public void eventConstructor() {
        char[][] seatingChart = {
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'},
                {'o', ' ', ' ', ' ', 'o'}};
        long date = ChronoUnit.DAYS.between(
                LocalDate.ofEpochDay(0), LocalDate.now()
        );
        Event e = new Event("DJ Khalid", 10.0, seatingChart, 360, date);
        assertEquals("Testing event name","DJ Khalid", e.getEventName());
        assertEquals("Testing price of event",10.0, e.getPriceOfEvent(), 0.001);
        assertEquals("Testing time of day", 360, e.getTimeOfDay());
        assertEquals("Testing date",date, e.getDay());
        assertArrayEquals("Testing seating chart", seatingChart, e.getSeatingChart());
    }

    @Test
    public void eventSetters() {
        char[][] seatingChart = {
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'x', 'o'},
                {'o', ' ', ' ', ' ', 'o'}};
        long date = ChronoUnit.DAYS.between(
                LocalDate.ofEpochDay(0), LocalDate.now()
        );
        Event e = new Event("Drake", 10.0, seatingChart, 360, date);
        e.setEventName("Benson Boone");
        e.setPriceOfEvent(15.0);
        e.setTimeOfDay(420);
        e.setDay(date + 1);
        char[][] newChart = {
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o'}};
        e.setSeatingChart(newChart);

        assertEquals("Benson Boone", e.getEventName());
        assertEquals(15.0, e.getPriceOfEvent(), 0.001);
        assertEquals(420, e.getTimeOfDay());
        assertEquals(date + 1, e.getDay());
        assertArrayEquals(newChart, e.getSeatingChart());
    }

    @Test
    public void testToString() {
        User u = new User("bob", "pw");
        long date = ChronoUnit.DAYS.between(
                LocalDate.ofEpochDay(0), LocalDate.now()
        );
        Event e = new Event("Concert", 20.0,
                new char[][]{
                           {'o', 'o', 'o', 'o'},
                        {'o', 'o', 'o', 'o', 'o'},
                        {'o', 'o', 'o', 'o', 'o'},
                           {'o', 'o', 'o', 'o'}},
                405, date);

        e.createReservation(new int[]{0,0}, new int[]{1,2}, u, 2, 405, date);


    }
}

