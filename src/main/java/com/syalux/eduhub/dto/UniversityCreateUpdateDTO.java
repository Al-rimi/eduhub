package com.syalux.eduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class UniversityCreateUpdateDTO {
    private String name;
    private String description;
    private String location;
    private String imageUrl;
    private List<String> requirements;
}
