import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {
    public void eventConstructor() {
        char[][] chart = {{'x','y'}};
        Event e = new Event("Movie", 10.0, chart, 1000L, 20L);
        assertEquals("Movie", e.getEventName());
        assertEquals(10.0, e.getPriceOfEvent(), 0.001);
        assertEquals(1000L, e.getTimeOfDay());
        assertEquals(10L, e.getDay());
        assertArrayEquals(chart, e.getSeatingChart());
    }

    public void eventSetters() {
        char[][] chart = {{'x','y'}};
        Event e = new Event("Movie", 10.0, chart, 1000L, 20L);
        e.setEventName("Show");
        e.setPriceOfEvent(15.0);
        e.setTimeOfDay(2000L);
        e.setDay(50L);
        char[][] newChart = {{'x'}};
        e.setSeatingChart(newChart);

        assertEquals("Show", e.getEventName());
        assertEquals(15.0, e.getPriceOfEvent(), 0.001);
        assertEquals(2000L, e.getTimeOfDay());
        assertEquals(50L, e.getDay());
        assertArrayEquals(newChart, e.getSeatingChart());
    }
}
