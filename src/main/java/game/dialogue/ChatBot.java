package game.dialogue;
import java.util.Scanner;
import game.logic.*;
import java.util.List;
public class ChatBot {
    final private PasswordProcessor passwordProcessor;

    public ChatBot() {
        this.passwordProcessor = new PasswordProcessor();
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Password Game! Please, enter /start to play the game!");

        while (true) {
            String input = scanner.nextLine();

            if ("/start".equals(input)) {
                playGame(scanner);
                break;
            } else if ("/help".equals(input)) {
                System.out.println("This is a password game. You must type a password and press ENTER.");
            } else {
                System.out.println("Invalid command. Type /start to play or /help for instructions.");
            }
        }

        scanner.close();
    }

    private void playGame(Scanner scanner) {
        while (true) {
            System.out.println("Welcome to the Password Game! Please, enter a password: ");
            String password = scanner.nextLine();

            List<String> rulesStatus = passwordProcessor.processAndGetStatus(password);
            boolean rulesSatisfied = passwordProcessor.isFinished();

            if (rulesSatisfied) {
                System.out.println("Congratulations! You've passed all the rules.");
                break;
            }

            System.out.println("Updated Rule Status:");
            rulesStatus.forEach(System.out::println);
        }
    }


    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        chatBot.startGame();
    }
}
