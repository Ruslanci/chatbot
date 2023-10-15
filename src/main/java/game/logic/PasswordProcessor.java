package game.logic;

import java.util.LinkedList;
import java.util.stream.Stream;

public class PasswordProcessor {
    private RulesSequence rulesSequence;
    private LinkedList<Rule> openedRules;
    public PasswordProcessor() {
        rulesSequence = new RulesSequence();
        openedRules = new LinkedList<Rule>();
        getNextRule();
    }
    private void getNextRule() {
        if (!rulesSequence.isEmpty())
            openedRules.add(rulesSequence.takeNewRule());
    }
    private Stream<Boolean> checkAllRules(String password) {
        return openedRules.stream().map(rule -> rule.match(password));
    }
    private void process(String password) {
        if (checkAllRules(password).allMatch(rule -> rule)) {
            getNextRule();
        }
    }
    private String[] getRulesStatus(String password) {
        return openedRules.stream().map(rule -> {
            if (rule.match(password))
                return "+ " + rule.Description;
            return "- " + rule.Description;
        }).toArray(String[]::new);
    }
    public String[] processAndGetStatus(String password) {
        this.process(password);
        return this.getRulesStatus(password);
    }
}
