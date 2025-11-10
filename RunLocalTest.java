import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.runners.Enclosed;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;


import java.io.*;
import java.util.ArrayList;


import static org.junit.Assert.*;


public class RunLocalTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - All Phase 1 ran succesfully!");
        } else {
            System.out.println("Some tests failed:");
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        private final PrintStream originalOutput = Stream.out;
        private final InputStream originalSysin = System.in;


        private ByteArrayInputStream testIn;
        private ByteArrayOutputStream testOut;


        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }


        public void restoreInputOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }


        public void recieveInput(String input) {
            testIn = new ByteArrayInputStream(input.getBytes());
            System.setIn(testIn);
        }


        public String getOutput() {
            return testOut.toString();
        }
    }

    public void reservationConstructor() {
        int[][] seats = {{1,0}, {0,1}};
        Reservation r = new Reservation("bob", 2, 100L, 200L, 20.0, seats);

        assertEquals("Username incorrect", "bob", r.getUser());
        assertEquals("Numpeople incorrect", 2, r.getNumPeople());
        assertEquals("Time incorrect", 100L, r.getTimeOfReservation());
        assertEquals("Date incorrect", 200L, r.getDate());
        assertEquals("Price incorrect", 20.0, r.getPrice(), 0.001);
        assertArrayEquals("Seats incorrect", seats, r.getReservedSeats());
    }

    public void reservationSetters() {
        Reservation r = new Reservation("", 0, 0, 0, 0, new int[][]{{0}});
        r.setUser("bob");
        r.setNumPeople(3);
        r.setTimeOfReservation(50L);
        r.setDate(100L);
        r.setPrice(50.0);
        int[][] newSeats = {{1,1}};
        r.setReservedSeats(newSeats);

        assertEquals("Setter failed", "bob", r.getUser());
        assertEquals("Setter failed", 3, r.getNumPeople());
        assertEquals("Setter failed", 50L, r.getTimeOfReservation());
        assertEquals("Setter failed", 100L, r.getDate());
        assertEquals("Setter failed", 50.0, r.getPrice(), 0.001);
        assertArrayEquals("Setter failed", newSeats, r.getReservedSeats());
    }


    public void eventConstructor() {
        char[][] chart = {{'x','y'}};
        Event e = new Event("Movie", 10.0, chart, 1000L, 20L);

        assertEquals("Movie", e.getEventNAme());
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

