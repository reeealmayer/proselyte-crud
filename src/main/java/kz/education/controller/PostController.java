package kz.education.controller;

import kz.education.model.Label;
import kz.education.model.Post;
import kz.education.model.Status;
import kz.education.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.getAll()
                .stream()
                .filter(post -> post.getStatus() != Status.DELETED)
                .toList();
    }

    public Post getById(Long id) {
        return postRepository.getById(id);
    }

    public Post create(String title, String content, List<Label> labels) {
        Post post = Post.builder()
                .content(content)
                .title(title)
                .status(Status.ACTIVE)
                .labels(labels)
                .build();
        return postRepository.save(post);
    }

    public Post update(Long id, String title, String content, List<Label> labels) {
        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
        if (!labels.isEmpty()) {
            post.setLabels(labels);
        }
        return postRepository.update(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public Post addLabelsToPost(Long postId, List<Label> labels) {
        Post post = postRepository.getById(postId);
        post.getLabels().addAll(labels);
        return postRepository.update(post);
    }
}
