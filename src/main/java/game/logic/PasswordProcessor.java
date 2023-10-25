package game.logic;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PasswordProcessor {
    private Queue<Rule> qRules;
    private LinkedList<Rule> openedRules;
    public PasswordProcessor() {
        qRules = RulesSequence.GetRulesQueue();
        openedRules = new LinkedList<Rule>();
        getNextRule();
    }
    private void getNextRule() {
        if (!qRules.isEmpty())
            openedRules.add(qRules.remove());

    }

    public boolean isFinished() {
        return openedRules.isEmpty();
    }
    private Stream<Boolean> checkAllRules(String password) {
        return openedRules.stream().map(rule -> rule.match(password));
    }
    private void process(String password) {
        while (checkAllRules(password).allMatch(rule -> rule) && !qRules.isEmpty()) {
            getNextRule();
        }
    }
    private LinkedList<String> getRulesStatus(String password) {
        return openedRules.stream().map(rule -> {
            if (rule.match(password))
                return "+ " + rule.description;
            return "- " + rule.description;
        }).collect(Collectors.toCollection(LinkedList<String>::new));
    }
    public List<String> processAndGetStatus(String password) {
        this.process(password);
        return this.getRulesStatus(password);
    }
}