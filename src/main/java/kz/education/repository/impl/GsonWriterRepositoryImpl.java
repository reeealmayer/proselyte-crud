package kz.education.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kz.education.exception.EntityNotFoundException;
import kz.education.model.Status;
import kz.education.model.Writer;
import kz.education.repository.WriterRepository;
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

public class GsonWriterRepositoryImpl implements WriterRepository {
    private static final Path WRITER_STORAGE_PATH = Path.of(Constants.WRITER_FILE_PATH);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Writer getById(Long id) {
        return loadFromFile().stream()
                .filter(existingWriter -> Objects.equals(existingWriter.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(Writer.class, id));
    }

    @Override
    public List<Writer> getAll() {
        return loadFromFile();
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> existingWriters = loadFromFile();
        Long nextId = getNextId(existingWriters);
        writer.setId(nextId);
        existingWriters.add(writer);
        saveToFile(existingWriters);
        return writer;
    }

    @Override
    public Writer update(Writer writer) {
        List<Writer> updatedWriters = loadFromFile().stream()
                .map(existingWriter -> {
                    if (Objects.equals(existingWriter.getId(), writer.getId())) {
                        writer.setStatus(existingWriter.getStatus());
                        return writer;
                    } else {
                        return existingWriter;
                    }
                }).toList();
        saveToFile(updatedWriters);
        return writer;
    }

    @Override
    public void deleteById(Long id) {
        List<Writer> updatedWriters = loadFromFile().stream()
                .peek(existingWriter -> {
                    if (Objects.equals(existingWriter.getId(), id)) {
                        existingWriter.setStatus(Status.DELETED);
                    }
                }).toList();
        saveToFile(updatedWriters);
    }

    private List<Writer> loadFromFile() {
        try {
            String json = Files.readString(WRITER_STORAGE_PATH, StandardCharsets.UTF_8);
            if (!json.isEmpty()) {
                Type listType = new TypeToken<List<Writer>>() {
                }.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения writers из файла");
        }
        return new ArrayList<>();
    }

    private void saveToFile(List<Writer> writers) {
        try {
            String json = gson.toJson(writers);
            Files.writeString(WRITER_STORAGE_PATH,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения writers в файл");
        }
    }

    private Long getNextId(List<Writer> writers) {
        return writers.stream()
                .map(Writer::getId)
                .max(Long::compare).orElse(0L) + 1;
    }
}
