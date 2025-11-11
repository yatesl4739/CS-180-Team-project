import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class VenueTest {



    private Venue venue;

    @BeforeAll
    public static void setupSaveDirectory() {
        File dir = new File("saveFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @BeforeEach
    public void setUp() {
        // Use the persistent EventDatabase inside Venue
        venue = new Venue("Test Venue");
    }

    @Test
    public void testVenueConstructorDefault() {
        assertEquals("Test Venue", venue.getVenueName());
        assertEquals("Not specified", venue.getHoursOfOperation());
        assertNotNull(venue.getEventDatabase());
    }

    @Test
    public void testSetAndGetHoursOfOperation() {
        venue.setHoursOfOperation("9am-9pm");
        assertEquals("9am-9pm", venue.getHoursOfOperation());
    }

    @Test
    public void testAddAndRemoveEvent() {
        int initialCount = venue.getEventDatabase().getEvents().size();

        Event e1 = new Event("Comedy Night", 30.0, new char[2][2], 1234L, 1L);
        venue.addEvent(e1);

        // Expect 1 new event on top of whatever persisted
        assertEquals(initialCount + 1, venue.getEventDatabase().getEvents().size());

        // Remove the newly added event
        venue.removeEvent(venue.getEventDatabase().getEvents().size() - 1);
        assertEquals(initialCount, venue.getEventDatabase().getEvents().size());
    }

    @Test
    public void testGetEventNames() {
        int existingCount = venue.getEventDatabase().getEvents().size();

        Event e1 = new Event("Concert", 25.0, new char[2][2], 1000L, 1L);
        Event e2 = new Event("Play", 15.0, new char[2][2], 2000L, 2L);
        venue.addEvent(e1);
        venue.addEvent(e2);

        ArrayList<String> names = venue.getEventNames();

        // Expect existing count + 2 new events
        assertTrue(names.size() >= existingCount + 2);
        assertTrue(names.contains("Concert"));
        assertTrue(names.contains("Play"));
    }

    @Test
    public void testGetEventTimesAndPrices() {
        int existingCount = venue.getEventDatabase().getEvents().size();

        Event e1 = new Event("Game Night", 10.0, new char[2][2], 3000L, 3L);
        Event e2 = new Event("Theatre", 40.0, new char[2][2], 4000L, 4L);
        venue.addEvent(e1);
        venue.addEvent(e2);

        ArrayList<Long> times = venue.getEventTimes();
        ArrayList<Double> prices = venue.getEventPrices();

        // Expect at least existingCount + 2 events after adding
        assertTrue(times.size() >= existingCount + 2);
        assertTrue(prices.size() >= existingCount + 2);

        // Validate the new data was added
        assertTrue(prices.contains(10.0));
        assertTrue(prices.contains(40.0));
        assertTrue(times.contains(3000L));
        assertTrue(times.contains(4000L));
    }

    @Test
    public void testGetAndSetVenueName() {
        venue.setVenueName("New Venue Name");
        assertEquals("New Venue Name", venue.getVenueName());
    }

}
