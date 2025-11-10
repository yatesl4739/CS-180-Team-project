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
}

