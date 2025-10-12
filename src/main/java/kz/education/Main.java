package kz.education;

import kz.education.context.ApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.getInstance();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1. Управление Labels");
            System.out.println("2. Управление Posts");
            System.out.println("3. Управление Writers");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> applicationContext.getLabelView().showMenu();
                case "2" -> applicationContext.getPostView().showMenu();
                case "3" -> applicationContext.getWriterView().showMenu();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

}