import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;


public class VenueTest {



    // Tests the constructor with only a name
    @Test
    public void testVenueConstructorDefault() {
        Venue v = new Venue("AMC");
        assertEquals("AMC", v.getVenueName());
        assertEquals("Not specified", v.getHoursOfOperation());
        assertNotNull(v.getEventDatabase());
        assertTrue(v.getEventDatabase().getEvents().isEmpty());
    }

    // Tests the full constructor with EventDatabase and hours
    @Test
    public void testVenueConstructorFull() {
        EventDatabase db = new EventDatabase();
        Venue v = new Venue("Regal", db, "9AM - 11PM");
        assertEquals("Regal", v.getVenueName());
        assertEquals("9AM - 11PM", v.getHoursOfOperation());
        assertEquals(db, v.getEventDatabase());
    }

    @Test
    void testGetEventPrices() {
        Venue venue = new Venue("Sample Venue");

        // Create dummy seating chart (small 2x2 array for simplicity)
        char[][] seating = {
                {'o', 'o'},
                {'o', 'o'}
        };

        // Create three events with different prices
        Event event1 = new Event("Concert", 50.0, seating, 1700000000L, 20251112L);
        Event event2 = new Event("Play", 30.0, seating, 1700001000L, 20251115L);
        Event event3 = new Event("Comedy Show", 40.0, seating, 1700002000L, 20251120L);

        // Add events to the venue
        venue.addEvent(event1);
        venue.addEvent(event2);
        venue.addEvent(event3);

        // Get all event prices
        ArrayList<Double> prices = venue.getEventPrices();

        // Assertions
        assertNotNull(prices, "Event prices list should not be null");
        assertEquals(3, prices.size(), "There should be 3 prices in the list");
        assertTrue(prices.contains(50.0), "Price 50.0 should exist in the list");
        assertTrue(prices.contains(30.0), "Price 30.0 should exist in the list");
        assertTrue(prices.contains(40.0), "Price 40.0 should exist in the list");
    }


    // Tests adding and removing events
    @Test
    public void testVenueAddAndRemoveEvent() {
        Venue v = new Venue("Cinemark");
        Event e = new Event("Movie Night", 10.0, new char[5][5], 1600, 120);
        v.addEvent(e);
        assertFalse(v.getEventDatabase().getEvents().isEmpty());
        v.removeEvent(0);
        assertTrue(v.getEventDatabase().getEvents().isEmpty());
    }

    // Tests getting event names
    @Test
    public void testGetEventNames() {
        Venue v = new Venue("AMC");
        v.addEvent(new Event("Concert", 20.0, new char[5][5], 1500, 110));
        v.addEvent(new Event("Play", 15.0, new char[5][5], 1700, 111));
        ArrayList<String> names = v.getEventNames();
        assertTrue(names.contains("Concert"));
        assertTrue(names.contains("Play"));
    }

    // Tests getting event times and prices
    @Test
    public void testGetEventTimesAndPrices() {
        Venue v = new Venue("Regal");
        v.addEvent(new Event("Concert", 20.0, new char[5][5], 1500, 110));
        v.addEvent(new Event("Play", 15.0, new char[5][5], 1700, 111));
        ArrayList<Long> times = v.getEventTimes();
        ArrayList<Double> prices = v.getEventPrices();
        assertEquals(2, times.size());
        assertEquals(2, prices.size());
    }
}
