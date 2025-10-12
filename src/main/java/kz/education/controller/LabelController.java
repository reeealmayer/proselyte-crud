package kz.education.controller;

import kz.education.model.Label;
import kz.education.model.Status;
import kz.education.repository.LabelRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LabelController {
    private final LabelRepository labelRepository;

    public List<Label> getAll() {
        return labelRepository.getAll()
                .stream()
                .filter(label -> label.getStatus() != Status.DELETED)
                .toList();
    }

    public Label getById(Long id) {
        return labelRepository.getById(id);
    }

    public Label create(String name) {
        Label label = Label.builder()
                .name(name)
                .status(Status.ACTIVE)
                .build();
        return labelRepository.save(label);
    }

    public Label update(Long id, String name) {
        Label label = Label.builder()
                .id(id)
                .name(name)
                .build();
        return labelRepository.update(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
