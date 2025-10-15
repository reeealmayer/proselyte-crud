package kz.education.view;

import kz.education.controller.PostController;
import kz.education.controller.WriterController;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Label;
import kz.education.model.Post;
import kz.education.model.Status;
import kz.education.model.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriterView {
    private final WriterController writerController;
    private final PostController postController;
    private final Scanner scanner = new Scanner(System.in);

    public WriterView(WriterController writerController, PostController postController) {
        this.writerController = writerController;
        this.postController = postController;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Writer Management ===");
            System.out.println("1. Показать все Writers");
            System.out.println("2. Найти Writer по ID");
            System.out.println("3. Создать Writer");
            System.out.println("4. Обновить Writer");
            System.out.println("5. Удалить Writer");
            System.out.println("6. Добавить Posts к Writer");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "6" -> addPostsToWriter();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void showAll() {
        List<Writer> writers = writerController.getAll();
        if (writers.isEmpty()) {
            System.out.println("Нет Writers.");
        } else {
            System.out.println("\nСписок Writers:");
            writers.stream()
                    .forEach(System.out::println);
        }
    }

    private void getById() {
        System.out.print("Введите ID Writer: ");
        Long id = Long.parseLong(scanner.nextLine());
        try {
            Writer writer = writerController.getById(id);
            System.out.println("Найдено: " + writer);
        } catch (EntityNotFoundException e) {
            System.err.println("Writer с ID " + id + " не найден");
        }
    }

    private void create() {
        System.out.print("Введите first name нового Writer: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите last name нового Writer: ");
        String lastName = scanner.nextLine();
        List<Post> posts = createPosts();
        Writer created = writerController.create(firstName, lastName, posts);
        System.out.println("Создано: " + created);
    }

    private void update() {
        System.out.print("Введите ID Writer для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новый first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите новый last name: ");
        String lastName = scanner.nextLine();

        List<Post> posts = createPosts();

        Writer updated = writerController.update(id, firstName, lastName, posts);
        System.out.println("Обновлено: " + updated);
    }

    private List<Post> createPosts() {
        List<Post> posts = new ArrayList<>();

        System.out.println("=== Создание Posts ===");
        while (true) {
            System.out.print("Введите title Post (или '0' для завершения): ");
            String title = scanner.nextLine().trim();

            if ("0".equals(title)) {
                break;
            }

            if (title.isEmpty()) {
                System.out.println("title не может быть пустым. Попробуйте снова.");
                continue;
            }

            System.out.print("Введите content Post: ");
            String content = scanner.nextLine().trim();

            if (content.isEmpty()) {
                System.out.println("content не может быть пустым. Попробуйте снова.");
                continue;
            }

            System.out.print("Введите id Post ");
            Long id = scanner.nextLong();

            Post post = Post.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .status(Status.ACTIVE).build();
            posts.add(post);
            System.out.println("Post добавлен: " + title);
        }
        return posts;
    }

    private void delete() {
        System.out.print("Введите ID Writer для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        writerController.delete(id);
        System.out.println("Writer удалён.");
    }

    private void addPostsToWriter() {
        System.out.print("Введите ID Writer для которого нужно добавить Posts: ");
        Long id = Long.parseLong(scanner.nextLine());
        Writer writer = null;
        try {
            writer = writerController.getById(id);
            System.out.println("Найдено: " + writer);
        } catch (EntityNotFoundException e) {
            System.err.println("Writer с ID " + id + " не найден");
            return;
        }

        List<Post> existingPosts = postController.getAll();
        System.out.println("Возможные Posts, которые можно добавить:");
        existingPosts.forEach(System.out::println);
        System.out.print("Введите ID Post'ов через запятую: ");
        String[] parts = scanner.nextLine().split(",");

        try {
            List<Post> posts = new ArrayList<>();
            for (String part : parts) {
                posts.add(postController.getById(Long.parseLong(part.trim())));
            }
            Writer updated = writerController.addPostsToWriter(writer.getId(), posts);
            System.out.println("Posts добавлены. Обновленный Writer: " + updated);
        } catch (EntityNotFoundException e) {
            System.err.println("Ошибка: один из указанных ID не найден.");
        }

    }
}
