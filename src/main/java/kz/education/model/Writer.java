package kz.education.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;
}
