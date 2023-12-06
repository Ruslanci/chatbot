package game.logic.rules;

import game.logic.Rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class EmojiIncludingRule implements Rule {
  @Override
  public Boolean match(String password) {
    Pattern emoji = Pattern.compile("[\\p{So}]");
    Matcher passwordMatcher = emoji.matcher(password);
    return passwordMatcher.find();
  }

  @Override
  public String getDescription() {
    return "Add an emoji to your password for extra flair!";
  }
}