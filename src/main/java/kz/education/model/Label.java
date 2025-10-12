package kz.education.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Label {
    private Long id;
    private String name;
    private Status status;
}
