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
}

