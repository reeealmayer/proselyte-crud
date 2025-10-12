package kz.education.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Label;
import kz.education.model.Status;
import kz.education.repository.LabelRepository;
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

public class GsonLabelRepositoryImpl implements LabelRepository {
    private static final Path LABEL_STORAGE_PATH = Path.of(Constants.LABEL_FILE_PATH);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public GsonLabelRepositoryImpl() {
    }


    @Override
    public List<Label> getAll() {
        return loadFromFile();
    }

    @Override
    public Label getById(Long id) throws EntityNotFoundException {
        List<Label> existingLabels = loadFromFile();
        return existingLabels.stream()
                .filter(label -> Objects.equals(label.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(Label.class, id));
    }

    @Override
    public Label save(Label label) {
        List<Label> existingLabels = loadFromFile();
        Long nextId = getNextId(existingLabels);
        label.setId(nextId);
        existingLabels.add(label);
        saveToFile(existingLabels);
        return label;
    }

    @Override
    public Label update(Label label) {
        List<Label> updatedLabels = loadFromFile().stream()
                .map(existingLabel -> {
                    if (Objects.equals(existingLabel.getId(), label.getId())) {
                        label.setStatus(existingLabel.getStatus());
                        return label;
                    } else {
                        return existingLabel;
                    }
                }).toList();
        saveToFile(updatedLabels);
        return label;
    }

    @Override
    public void deleteById(Long id) {
        List<Label> updatedLabels = loadFromFile().stream()
                .peek(existingLabel -> {
                    if (Objects.equals(existingLabel.getId(), id)) {
                        existingLabel.setStatus(Status.DELETED);
                    }
                }).toList();
        saveToFile(updatedLabels);
    }

    private Long getNextId(List<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .max(Long::compare).orElse(0L) + 1;
    }

    private List<Label> loadFromFile() {
        try {
            String json = Files.readString(LABEL_STORAGE_PATH, StandardCharsets.UTF_8);
            if (!json.isEmpty()) {
                Type listType = new TypeToken<List<Label>>() {
                }.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения labels из файла");
        }
        return new ArrayList<>();
    }

    private void saveToFile(List<Label> labels) {
        try {
            String json = gson.toJson(labels);
            Files.writeString(LABEL_STORAGE_PATH,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения labels в файл");
        }
    }
}
