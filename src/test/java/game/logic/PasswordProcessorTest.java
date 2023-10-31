package game.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
public class PasswordProcessorTest {

    @Test
    public void testProcessAndGetStatus_AllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        List<String> status = passwordProcessor.ProcessAndGetStatus("GoodPassword##123");

        assertEquals(4, status.size());
        assertEquals("+ Your password must be at least 5 characters", status.get(0));
        assertEquals("+ Your password must include a number", status.get(1));
        assertEquals("+ Your password must include an uppercase letter", status.get(2));
        assertEquals("+ Your password must include a special character", status.get(3));

    }

    @Test
    public void testProcessAndGetStatus_NotAllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        List<String> status = passwordProcessor.ProcessAndGetStatus("WeakPassword");

        assertEquals(4, status.size()); // Assuming there are three rules in your RulesSequence
        assertEquals("+ Your password must be at least 5 characters", status.get(0));
        assertEquals("- Your password must include a number", status.get(1));
        assertEquals("+ Your password must include an uppercase letter", status.get(2));
        assertEquals("- Your password must include a special character", status.get(3));


    }

    @Test
    public void testRulesSatisfied_AllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        passwordProcessor.ProcessAndGetStatus("GoodPas(#33sword123");

        assertTrue(passwordProcessor.RulesSatisfied());
    }

    @Test
    public void testRulesSatisfied_NotAllRulesSatisfied() {
        PasswordProcessor passwordProcessor = new PasswordProcessor();
        passwordProcessor.ProcessAndGetStatus("WeakPassword");

        assertFalse(passwordProcessor.RulesSatisfied());
    }
}