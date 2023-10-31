package game.dialogue;
import java.util.Scanner;
import game.logic.*;
import java.util.List;
public class ChatBot {
    private PasswordProcessor passwordProcessor;

    public ChatBot() {
        this.passwordProcessor = new PasswordProcessor();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Password Game! Please, enter a password: ");
            String password = scanner.nextLine();

            List<String> rulesStatus = passwordProcessor.processAndGetStatus(password);
            boolean rulesSatisfied = passwordProcessor.RulesSatisfied();

            if (rulesSatisfied) {
                System.out.println("Congratulations! You've passed all the rules.");
                break;
            }

            System.out.println("Updated Rule Status:");
            rulesStatus.forEach(System.out::println);
        }

        scanner.close();
    }

    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        chatBot.startChat();
    }
}