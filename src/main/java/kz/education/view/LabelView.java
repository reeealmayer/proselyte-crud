package kz.education.view;

import kz.education.controller.LabelController;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Label;

import java.util.List;
import java.util.Scanner;

public class LabelView {
    private final LabelController labelController;
    private final Scanner scanner = new Scanner(System.in);

    public LabelView(LabelController labelController) {
        this.labelController = labelController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Label Management ===");
            System.out.println("1. Показать все Labels");
            System.out.println("2. Найти Label по ID");
            System.out.println("3. Создать Label");
            System.out.println("4. Обновить Label");
            System.out.println("5. Удалить Label");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void showAll() {
        List<Label> labels = labelController.getAll();
        if (labels.isEmpty()) {
            System.out.println("Нет Labels.");
        } else {
            System.out.println("\nСписок Labels:");
            labels.stream()
                    .forEach(System.out::println);
        }
    }

    private void getById() {
        System.out.print("Введите ID Label: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            Label label = labelController.getById(id);
            System.out.println("Найдено: " + label);
        } catch (EntityNotFoundException e) {
            System.err.println("Label с ID " + id + " не найден");
        }
    }

    private void create() {
        System.out.print("Введите имя нового Label: ");
        String name = scanner.nextLine();
        Label created = labelController.create(name);
        System.out.println("Создано: " + created);
    }

    private void update() {
        System.out.print("Введите ID Label для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новое имя: ");
        String name = scanner.nextLine();

        Label updated = labelController.update(id, name);
        System.out.println("Обновлено: " + updated);
    }

    private void delete() {
        System.out.print("Введите ID Label для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        labelController.delete(id);
        System.out.println("Label удалён.");
    }
}
