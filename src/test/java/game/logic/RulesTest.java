package game.logic;
import game.logic.rules.*;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class RulesTest {
    Rule lenghtRule = new FiveCharactersRule();
    Rule digitRule = new DigitsIncludingRule();
    Rule upperRule = new UpperCaseIncludingRule();
    Rule specCharRule = new SpecialIncludingRule();
    Rule colorRule = new ColorfulRule();
    Rule dateRule = new CurrentDateIncludingRule();
    Rule palindromeRule = new PalindromeRule();
    Rule emojiRule = new EmojiIncludingRule();
    @Test
    public void fiveCharactersRuleTestTrue() {
        Assert.assertTrue(lenghtRule.match("qwert"));
    }
    @Test
    public void fiveCharactersRuleTestFalse() {
        Assert.assertFalse(lenghtRule.match("qwer"));
    }
    @Test
    public void digitsIncludingRuleTestTrue() {
        Assert.assertTrue(digitRule.match("123"));
    }
    @Test
    public void digitsIncludingRuleTestFalse() {
        Assert.assertFalse(digitRule.match("abc"));
    }
    @Test
    public void upperCaseIncludingRuleTestTrue() {
        Assert.assertTrue(upperRule.match("ABC"));
    }
    @Test
    public void upperCaseIncludingRuleTestFalse() {
        Assert.assertFalse(upperRule.match("abc"));
    }
    @Test
    public void specialIncludingTestTrue() {
        Assert.assertTrue(specCharRule.match("!@#$"));
    }
    @Test
    public void specialIncludingTestFalse() {
        Assert.assertFalse(specCharRule.match("1234"));
    }
    @Test
    public void colorfulTestTrue() {
        Assert.assertTrue(colorRule.match("red"));
    }
    @Test
    public void colorfulTestFalse() {
        Assert.assertFalse(colorRule.match("qwer"));
    }
    @Test
    public void currentDateIncludingTestTrue() {
        Assert.assertTrue(dateRule.match(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    }
    @Test
    public void currentDateIncludingTestFalse() {
        Assert.assertFalse(dateRule.match("qwer"));
    }
    @Test
    public void palindromeTestTrue() {
        Assert.assertTrue(palindromeRule.match("DoroD"));
    }
    @Test
    public void palindromeTestFalse() {
        Assert.assertFalse(palindromeRule.match("Tempet"));
    }
    @Test
    public void emojiTestTrue() {
        Assert.assertTrue(emojiRule.match("\uD83D\uDE3C"));
    }
    @Test
    public void emojiTestFalse() {
        Assert.assertFalse(emojiRule.match("qwer"));
    }
}
