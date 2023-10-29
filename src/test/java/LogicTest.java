import game.logic.RulesLogic;
import org.junit.Test;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.nio.charset.Charset;

public class LogicTest {
    private String RandomString(int strSize) {
        byte[] array = new byte[strSize];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.US_ASCII);
    }
    @Test
    public void rule1_Test() {
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(RulesLogic.rule1_fiveCharacters(RandomString(5)));
            Assert.assertFalse(RulesLogic.rule1_fiveCharacters(RandomString(4)));
        }
    }
    @Test
    public void rule2_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if (currentCharCode >= 48 && currentCharCode <= 57)
                Assert.assertTrue(RulesLogic.rule2_includesDigits(new String(new char[]{currentCharCode})));
            else
                Assert.assertFalse(RulesLogic.rule2_includesDigits(new String(new char[]{currentCharCode})));
        }
    }
    @Test
    public void rule3_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if (currentCharCode >= 65 && currentCharCode <= 90)
                Assert.assertTrue(RulesLogic.rule3_includesUpperCase(new String(new char[]{currentCharCode})));
            else
                Assert.assertFalse(RulesLogic.rule3_includesUpperCase(new String(new char[]{currentCharCode})));
        }
    }

    @Test
    public void rule4_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if ((currentCharCode >= 65 && currentCharCode <= 90) || (currentCharCode >= 97 && currentCharCode <= 122) || (currentCharCode >= 48 && currentCharCode <= 57))
                Assert.assertFalse(RulesLogic.rule4_includesSpecial(new String(new char[]{currentCharCode})));
            else
                Assert.assertTrue(RulesLogic.rule4_includesSpecial(new String(new char[]{currentCharCode})));
        }
    }
}
