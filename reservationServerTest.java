import static org.junit.Assert.*;

public class reservationServerTest {

    public void testConstructor() throws Exception {
        reservationServer s = new reservationServer();
        assertNotNull(s);
    }

    public void testStartStop() throws Exception {
        reservationServer s = new reservationServer();

        s.start();
        s.stop();
    }
}

