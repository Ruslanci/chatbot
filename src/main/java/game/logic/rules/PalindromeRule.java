package game.logic.rules;

import game.logic.Rule;

import java.util.LinkedList;
import java.util.Stack;


public class PalindromeRule implements Rule {
  @Override
  public Boolean match(String password) {
    for (int i = 0; i < password.length() / 2; i++) {
      char left_ch = password.charAt(i);
      char right_ch = password.charAt(password.length() - 1 - i);
      if (left_ch != right_ch) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getDescription() {
    return "Make your password a palindrome – a word that reads the same backward as forward.";
  }
}
