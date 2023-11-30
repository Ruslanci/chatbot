package game.logic;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
public class PasswordProcessorTest {

    @Test
    public void processAndGetStatusWeakPasswordTest() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        LinkedList<String> result = passwordProcessor.processAndGetStatus("abc");

        assertFalse(passwordProcessor.isFinished());
        assertEquals("- Your password must be at least 5 characters", result.get(0));
    }

    @Test
    public void processAndGetStatusStrongPasswordTest() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        LinkedList<String> result = passwordProcessor.processAndGetStatus("Abcde5#");

        assertTrue(passwordProcessor.isFinished());
        assertEquals("+ Your password must be at least 5 characters", result.get(0));
        assertEquals("+ Your password must include a number", result.get(1));
        assertEquals("+ Your password must include an uppercase letter", result.get(2));
        assertEquals("+ Your password must include a special character", result.get(3));
    }

    @Test
    public void processAndGetStatusIncompletePasswordTest() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        LinkedList<String> result = passwordProcessor.processAndGetStatus("Abcde5");

        assertFalse(passwordProcessor.isFinished());
        assertEquals("+ Your password must be at least 5 characters", result.get(0));
        assertEquals("+ Your password must include a number", result.get(1));
        assertEquals("+ Your password must include an uppercase letter", result.get(2));
        assertEquals("- Your password must include a special character", result.get(3));
    }
    @Test
    public void processAndGetStatusCompletedPasswordTest() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        LinkedList<String> result = passwordProcessor.processAndGetStatus("Abcde5#");

        assertTrue(passwordProcessor.isFinished());
        assertEquals("+ Your password must be at least 5 characters", result.get(0));
        assertEquals("+ Your password must include a number", result.get(1));
        assertEquals("+ Your password must include an uppercase letter", result.get(2));
        assertEquals("+ Your password must include a special character", result.get(3));
    }
}