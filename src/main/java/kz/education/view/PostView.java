package kz.education.view;

import kz.education.controller.LabelController;
import kz.education.controller.PostController;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Label;
import kz.education.model.Post;
import kz.education.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostView {
    private final PostController postController;
    private final LabelController labelController;
    private final Scanner scanner = new Scanner(System.in);

    public PostView(PostController postController, LabelController labelController) {
        this.postController = postController;
        this.labelController = labelController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Post Management ===");
            System.out.println("1. Показать все Posts");
            System.out.println("2. Найти Post по ID");
            System.out.println("3. Создать Post");
            System.out.println("4. Обновить Post");
            System.out.println("5. Удалить Post");
            System.out.println("6. Добавить Labels к Post");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "6" -> addLabelsToPost();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void showAll() {
        List<Post> posts = postController.getAll();
        if (posts.isEmpty()) {
            System.out.println("Нет Posts.");
        } else {
            System.out.println("\nСписок Posts:");
            posts.stream()
                    .forEach(System.out::println);
        }
    }

    private void getById() {
        System.out.print("Введите ID Post: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            Post post = postController.getById(id);
            System.out.println("Найдено: " + post);
        } catch (EntityNotFoundException e) {
            System.err.println("Post с ID " + id + " не найден");
        }
    }

    private void create() {
        System.out.print("Введите title нового Post: ");
        String title = scanner.nextLine();
        System.out.print("Введите content нового Post: ");
        String content = scanner.nextLine();
        List<Label> labels = createLabels();
        Post created = postController.create(title, content, labels);
        System.out.println("Создано: " + created);
    }

    private void update() {
        System.out.print("Введите ID Post для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новый title: ");
        String title = scanner.nextLine();

        System.out.print("Введите новый content: ");
        String content = scanner.nextLine();

        List<Label> labels = createLabels();

        Post updated = postController.update(id, title, content, labels);
        System.out.println("Обновлено: " + updated);
    }

    private List<Label> createLabels() {
        List<Label> labels = new ArrayList<>();

        System.out.println("=== Создание Labels ===");
        while (true) {
            System.out.print("Введите название Label (или '0' для завершения): ");
            String name = scanner.nextLine().trim();

            if ("0".equals(name)) {
                break;
            }

            if (name.isEmpty()) {
                System.out.println("Название не может быть пустым. Попробуйте снова.");
                continue;
            }

            System.out.print("Введите id Label ");
            Long id = scanner.nextLong();

            Label label = new Label(id, name, Status.ACTIVE);
            labels.add(label);
            System.out.println("Label добавлен: " + name);
        }
        return labels;
    }

    private void delete() {
        System.out.print("Введите ID Post для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        postController.delete(id);
        System.out.println("Post удалён.");
    }

    private void addLabelsToPost() {
        System.out.print("Введите ID Post для которого нужно добавить Labels: ");
        Long id = Long.parseLong(scanner.nextLine());
        Post post = null;
        try {
            post = postController.getById(id);
            System.out.println("Найдено: " + post);
        } catch (EntityNotFoundException e) {
            System.err.println("Post с ID " + id + " не найден");
            return;
        }

        List<Label> existingLabels = labelController.getAll();
        System.out.println("Возможные Labels, которые можно добавить:");
        existingLabels.forEach(System.out::println);
        System.out.print("Введите ID Label'ов через запятую: ");
        String[] parts = scanner.nextLine().split(",");

        try {
            List<Label> labels = new ArrayList<>();
            for (String part : parts) {
                labels.add(labelController.getById(Long.parseLong(part.trim())));
            }
            Post updated = postController.addLabelsToPost(post.getId(), labels);
            System.out.println("Labels добавлены. Обновленный пост: " + updated);
        } catch (EntityNotFoundException e) {
            System.err.println("Ошибка: один из указанных ID не найден.");
        }

    }
}
