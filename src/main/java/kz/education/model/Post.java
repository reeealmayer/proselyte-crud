package kz.education.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Post {
    private Long id;
    private String title;
    private String content;
    private List<Label> labels;
    private Status status;
}
