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
            Assert.assertEquals(true, RulesLogic.rule1_fiveCharacters(RandomString(5)));
            Assert.assertEquals(false, RulesLogic.rule1_fiveCharacters(RandomString(4)));
        }
    }
    @Test
    public void rule2_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if (currentCharCode >= 48 && currentCharCode <= 57)
                Assert.assertEquals(true, RulesLogic.rule2_includesDigits(new String(new char[] {currentCharCode})));
            else
                Assert.assertEquals(false, RulesLogic.rule2_includesDigits(new String(new char[] {currentCharCode})));
        }
    }
    @Test
    public void rule3_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if (currentCharCode >= 65 && currentCharCode <= 90)
                Assert.assertEquals(true, RulesLogic.rule3_includesUpperCase(new String(new char[] {currentCharCode})));
            else
                Assert.assertEquals(false, RulesLogic.rule3_includesUpperCase(new String(new char[] {currentCharCode})));
        }
    }

    @Test
    public void rule4_Test() {
        for (char currentCharCode = 0; currentCharCode < 65535; currentCharCode++) {
            if ((currentCharCode >= 65 && currentCharCode <= 90) || (currentCharCode >= 97 && currentCharCode <= 122) || (currentCharCode >= 48 && currentCharCode <= 57))
                Assert.assertEquals(false, RulesLogic.rule4_includesSpecial(new String(new char[] {currentCharCode})));
            else
                Assert.assertEquals(true, RulesLogic.rule4_includesSpecial(new String(new char[] {currentCharCode})));
        }
    }
}
