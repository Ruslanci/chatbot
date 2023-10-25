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
        System.out.println("Welcome to the Password Game! Please, enter a password: ");
        String password = scanner.nextLine();
        while (!passwordProcessor.isFinished()) {
            List<String> status = passwordProcessor.processAndGetStatus(password);


            if (status.contains("-")) {
                System.out.print("Please enter a password: ");
                password = scanner.nextLine();
                status = passwordProcessor.processAndGetStatus(password);

                System.out.println("Updated Rule Status:");
                status.forEach(System.out::println);
            } else {
                System.out.println("All rules have been satisfied.");
            }
        }

        System.out.println("Congratulations! You've passed all the rules.");
        scanner.close();
    }

    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        chatBot.startChat();
    }
}