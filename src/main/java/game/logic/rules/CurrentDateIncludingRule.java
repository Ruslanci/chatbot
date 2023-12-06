package game.logic.rules;
import game.logic.Rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CurrentDateIncludingRule implements Rule {
  @Override
  public Boolean match(String password) {
    String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    return password.contains(currentDate);
  }

  @Override
  public String getDescription() {
    return "Your password must include the current date in the format dd-mm-yyyy";
  }
}

