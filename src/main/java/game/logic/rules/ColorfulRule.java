package game.logic.rules;

import game.logic.Rule;

import java.util.Arrays;
import java.util.List;

public class ColorfulRule implements Rule {
  private final List<String> colorNames = Arrays.asList("red", "blue", "green", "yellow", "purple", "orange");

  @Override
  public Boolean match(String password) {
    return colorNames.stream().anyMatch(password::contains);
  }

  @Override
  public String getDescription() {
    return "Use at least one color name in your password.";
  }
}