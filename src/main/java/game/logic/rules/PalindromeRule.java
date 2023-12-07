package game.logic.rules;

import game.logic.Rule;

import java.util.LinkedList;
import java.util.Stack;


public class PalindromeRule implements Rule {
  @Override
  public Boolean match(String password) {
    String reversed = new StringBuilder(password).reverse().toString();
    return password.equals(reversed);
  }

  @Override
  public String getDescription() {
    return "Make your password a palindrome – a word that reads the same backward as forward.";
  }
}
