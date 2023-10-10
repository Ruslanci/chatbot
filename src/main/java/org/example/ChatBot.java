package org.example;
import java.util.Scanner;

public class ChatBot {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать! Я чат-бот.");
        System.out.println("Для начала разговора введите /start");

        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("/start")) {
                System.out.println("Я чатбот, который...");
                System.out.println("Для подробной помощи введите /help");
            } else if (userInput.equals("/help")) {
                System.out.println("Помощь...");
            } else {
                System.out.println("Извините, я не понимаю ваш запрос. Введите /help для помощи.");
            }
        }
    }
}