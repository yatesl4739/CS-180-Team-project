import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class reservationSeverTest {

    public void testConstructor() throws Exception {
        reservationServer s = new reservationServer(4242);
        assertNotNull(s);
    }

    public void testStartStop() throws Exception {
        reservationServer s = new reservationServer(4242);

        s.start();
        s.stop();
    }
}

