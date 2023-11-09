import game.logic.Rule;
import game.logic.RulesLogic;
import game.logic.rules.DigitsIncludingRule;
import game.logic.rules.FiveCharactersRule;
import game.logic.rules.SpecialIncludingRule;
import game.logic.rules.UpperCaseIncludingRule;
import org.junit.Test;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.nio.charset.Charset;

public class LogicTest {
    Rule rule1 = new FiveCharactersRule();
    Rule rule2 = new DigitsIncludingRule();
    Rule rule3 = new UpperCaseIncludingRule();
    Rule rule4 = new SpecialIncludingRule();
    @Test
    public void fiveCharactersRuleTestTrue() {
        Assert.assertTrue(rule1.match("qwert"));
    }
    @Test
    public void fiveCharactersRuleTestFalse() {
        Assert.assertFalse(rule1.match("qwer"));
    }
    @Test
    public void digitsIncludingRuleTestTrue() {
        Assert.assertTrue(rule2.match("123"));
    }
    @Test
    public void digitsIncludingRuleTestFalse() {
        Assert.assertFalse(rule2.match("abc"));
    }
    @Test
    public void upperCaseIncludingRuleTestTrue() {
        Assert.assertTrue(rule3.match("ABC"));
    }
    @Test
    public void upperCaseIncludingRuleTestFalse() {
        Assert.assertFalse(rule3.match("abc"));
    }
    @Test
    public void specialIncludingTestTrue() {
        Assert.assertTrue(rule4.match("!@#$"));
    }
    @Test
    public void specialIncludingTestFalse() {
        Assert.assertFalse(rule4.match("1234"));
    }
}
