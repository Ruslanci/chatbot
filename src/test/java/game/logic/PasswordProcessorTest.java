package game.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
public class PasswordProcessorTest {

    @Test
    public void testProcessAndGetStatusAllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        List<String> status = passwordProcessor.processAndGetStatus("GoodPassword##123");

        assertEquals(4, status.size());
        assertEquals("+ Your password must be at least 5 characters", status.get(0));
        assertEquals("+ Your password must include a number", status.get(1));
        assertEquals("+ Your password must include an uppercase letter", status.get(2));
        assertEquals("+ Your password must include a special character", status.get(3));

    }

    @Test
    public void testRulesSatisfiedAllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        passwordProcessor.processAndGetStatus("GoodPas(#33sword123");

        assertTrue(passwordProcessor.isFinished());
    }

    @Test
    public void testRulesSatisfiedNotAllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        passwordProcessor.processAndGetStatus("WeakPassword");

        assertFalse(passwordProcessor.isFinished());
    }
}