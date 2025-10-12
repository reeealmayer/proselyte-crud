package kz.education.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Post;
import kz.education.model.Status;
import kz.education.repository.PostRepository;
import kz.education.util.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GsonPostRepositoryImpl implements PostRepository {
    private static final Path POST_STORAGE_PATH = Path.of(Constants.POST_FILE_PATH);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Post getById(Long id) {
        return loadFromFile().stream()
                .filter(existingPost -> Objects.equals(existingPost.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(Post.class, id));
    }

    @Override
    public List<Post> getAll() {
        return loadFromFile();
    }

    @Override
    public Post save(Post post) {
        List<Post> existingPosts = loadFromFile();
        Long nextId = getNextId(existingPosts);
        post.setId(nextId);
        existingPosts.add(post);
        saveToFile(existingPosts);
        return post;
    }

    @Override
    public Post update(Post post) {
        List<Post> updatedPosts = loadFromFile().stream()
                .map(existingPost -> {
                    if (Objects.equals(existingPost.getId(), post.getId())) {
                        post.setStatus(existingPost.getStatus());
                        return post;
                    } else {
                        return existingPost;
                    }
                }).toList();
        saveToFile(updatedPosts);
        return post;
    }

    @Override
    public void deleteById(Long id) {
        List<Post> updatedPosts = loadFromFile().stream()
                .peek(existingPost -> {
                    if (Objects.equals(existingPost.getId(), id)) {
                        existingPost.setStatus(Status.DELETED);
                    }
                }).toList();
        saveToFile(updatedPosts);
    }

    private List<Post> loadFromFile() {
        try {
            String json = Files.readString(POST_STORAGE_PATH, StandardCharsets.UTF_8);
            if (!json.isEmpty()) {
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения posts из файла");
        }
        return new ArrayList<>();
    }

    private void saveToFile(List<Post> posts) {
        try {
            String json = gson.toJson(posts);
            Files.writeString(POST_STORAGE_PATH,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения posts в файл");
        }
    }

    private Long getNextId(List<Post> posts) {
        return posts.stream()
                .map(Post::getId)
                .max(Long::compare).orElse(0L) + 1;
    }
}
