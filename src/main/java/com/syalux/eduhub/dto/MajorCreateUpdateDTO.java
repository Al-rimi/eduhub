package com.syalux.eduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MajorCreateUpdateDTO {
    private String name;
    private String description;
    private Long universityId;
}
