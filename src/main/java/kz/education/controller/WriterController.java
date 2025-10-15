package kz.education.controller;

import kz.education.model.Post;
import kz.education.model.Status;
import kz.education.model.Writer;
import kz.education.repository.WriterRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WriterController {
    private final WriterRepository writerRepository;

    public List<Writer> getAll() {
        return writerRepository.getAll()
                .stream()
                .filter(writer -> writer.getStatus() != Status.DELETED)
                .toList();
    }

    public Writer getById(Long id) {
        return writerRepository.getById(id);
    }

    public Writer create(String firstName, String lastName, List<Post> posts) {
        Writer writer = Writer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .status(Status.ACTIVE)
                .posts(posts)
                .build();
        return writerRepository.save(writer);
    }

    public Writer update(Long id, String firstName, String lastName, List<Post> posts) {
        Writer writer = Writer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        if (!posts.isEmpty()) {
            writer.setPosts(posts);
        }
        return writerRepository.update(writer);
    }

    public void delete(Long id) {
        writerRepository.deleteById(id);
    }

    public Writer addPostsToWriter(Long writerId, List<Post> posts) {
        Writer writer = writerRepository.getById(writerId);
        writer.getPosts().addAll(posts);
        return writerRepository.update(writer);
    }
}
