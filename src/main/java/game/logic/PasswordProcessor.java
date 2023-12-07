package game.logic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PasswordProcessor {
    private Queue<Rule> unopennedRules;
    private LinkedList<Rule> openedRules;
    private Boolean completed;
    public PasswordProcessor() {
        unopennedRules = RulesSequence.getRulesQueue();
        openedRules = new LinkedList<Rule>();
        completed = false;
        getNextRule();
    }
    private void getNextRule() {
        if (!unopennedRules.isEmpty())
            openedRules.add(unopennedRules.remove());
    }

    public boolean isFinished() {
        return (unopennedRules.isEmpty() && completed);
    }
    private Stream<Boolean> checkAllRules(String password) {
        return openedRules.stream().map(rule -> rule.match(password));
    }
    private void process(String password) {
        while (checkAllRules(password).allMatch(rule -> rule) && !unopennedRules.isEmpty()) {
            getNextRule();
        }
        completed = (checkAllRules(password).allMatch(rule -> rule));
    }
    private LinkedList<String> getRulesStatus(String password) {
        return openedRules.stream().map(rule -> {
            if (rule.match(password))
                return "+ " + rule.getDescription();
            return "- " + rule.getDescription();
        }).collect(Collectors.toCollection(LinkedList<String>::new));
    }
    public LinkedList<String> processAndGetStatus(String password) {
        this.process(password);
        return this.getRulesStatus(password);
    }
}