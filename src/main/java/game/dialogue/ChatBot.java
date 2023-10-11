package game.dialogue;
import game.logic.RulesChecker;
import java.util.Scanner;

public class ChatBot {
    public static void main(String[] args) {

        System.out.println("Добро пожаловать! Я чат-бот.");
        System.out.println("Для начала разговора введите /start");

    }
    private void chatBotStart() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Я чатбот, который позволяет вам играть в Password Game! Для подробной помощи введите /help.");
        System.out.println("Игрок, введите пароль: ");
        String password = scanner.nextLine();

    }

    private void chatBotHelp() {
        System.out.println("Помощь...");
    }

    private void chatBotError() {
        System.out.println("Извините, я не понимаю ваш запрос. Введите /help для помощи.");
    }
}