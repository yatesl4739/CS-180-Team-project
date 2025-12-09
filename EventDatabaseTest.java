import org.junit.Test;
import static org.junit.Assert.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//This will test both the event database AND the event class

public class EventDatabaseTest {

    //First we do the event tests

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

    @Test
    public void testResrvationSystem() {
        User u = new User("jhon", "pw");
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

        assertEquals(2, e.getReservationDB().getReservations().getFirst().getNumPeople());

        assertEquals('x', e.getSeatingChart()[e.getReservationDB().getReservations().getFirst().getX()[0]]
                [e.getReservationDB().getReservations().getFirst().getY()[0]]);

    }


    //Now we do event database tests

    @Test
    public void databaseConstructor() {
        //test empty constructor
        EventDatabase ed = new EventDatabase();
        long date = ChronoUnit.DAYS.between(
                LocalDate.ofEpochDay(0), LocalDate.now()
        );

        Event singleEvent = new Event("taylor swift", 15.0, new char[][]{
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'}}, 540, date + 7);

        EventDatabase ed2 = new EventDatabase(singleEvent);

        int edSize = ed.getEvents().size();

        boolean edSizeBool = false;
        if (edSize == 0 || edSize == 2) {
            edSizeBool = true;
        }

        assertTrue("0 if its the first run, 2 if the test has been ran before and the database file exists",
                edSizeBool);

        edSizeBool = false;


        if (edSize == 0) {
            ed.addEvent(singleEvent);
        }
        ed.rmEvent(0);
        edSize = ed.getEvents().size();

        if (edSize == 0 || edSize == 1) {
            edSizeBool = true;
        }

        assertTrue("0 if its the first run, 1 if test has been ran and event database file extists",
                edSizeBool);

        assertEquals("taylor swift", ed2.getEvents().getFirst().getEventName());
    }

    @Test
    public void databaseMethods() {
        long date = ChronoUnit.DAYS.between(
                LocalDate.ofEpochDay(0), LocalDate.now()
        );
        Event singleEvent = new Event("taylor swift", 15.0, new char[][]{
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o'}}, 540, date + 7);
        Event nextEvent = new Event("Boiler pete", 50.0, new char[][]{
                {'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o'},
                {'o', 'x', 'x', 'o'}}, 720, date + 100);

        ArrayList<Event> listOfEvents = new ArrayList<Event>();
        listOfEvents.add(singleEvent);
        listOfEvents.add(nextEvent);

        EventDatabase ed3 = new EventDatabase(listOfEvents);
        assertEquals(50.0, ed3.getEvents().get(1).getPriceOfEvent(), .0001);

        ed3.rmEvent(1);

        assertEquals(1, ed3.getEvents().size());

        ed3.addEvent(nextEvent);

        assertEquals('x', ed3.getEvents().getLast().getSeatingChart()[3][2]);

        ed3.rmEvent(0);
        ed3.rmEvent(0);

        listOfEvents.add(singleEvent);
        listOfEvents.add(nextEvent);

        ed3.setEvents(listOfEvents);

        assertEquals(540, ed3.getEvents().getFirst().getTimeOfDay());


    }

}