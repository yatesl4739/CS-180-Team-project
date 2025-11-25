import org.junit.Test;
import static org.junit.Assert.*;

public class reservationServerTest {

    public void testConstructor() {
        reservationServer server = new reservationServer(5000);

        assertEquals(5000, server.getPort());
        assertFalse(server.isRunning());
        assertEquals(0, server.getClientCount());
        assertNotNull(server.getVenue1());
        assertNotNull(server.getUserDB());
    }

    @Test
    public void testStartSetsRunningTrue() {
        reservationServer server = new reservationServer(5001);

        server.start();

        assertTrue(server.isRunning());

        server.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceThrows() {
        reservationServer server = new reservationServer(5002);

        server.start();
        server.start();

        server.stop();
    }

    @Test
    public void testStopAfterStart() {
        reservationServer server = new reservationServer(5003);

        server.start();
        assertTrue(server.isRunning());

        server.stop();

        assertFalse(server.isRunning());
        assertEquals(0, server.getClientCount());
    }

    @Test
    public void testStopWithoutStart() {
        reservationServer server = new reservationServer(5004);

        server.stop();

        assertFalse(server.isRunning());
        assertEquals(0, server.getClientCount());
    }
}



