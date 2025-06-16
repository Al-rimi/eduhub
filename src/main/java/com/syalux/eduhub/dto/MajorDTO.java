package com.syalux.eduhub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MajorDTO {
    private Long id;
    private String name;
    private String description;
    private Long universityId;
    private String universityName;

    public MajorDTO(Long id, String name, String description, Long universityId, String universityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.universityId = universityId;
        this.universityName = universityName;
    }
}
