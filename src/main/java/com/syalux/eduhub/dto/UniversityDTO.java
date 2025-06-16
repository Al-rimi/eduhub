package com.syalux.eduhub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class UniversityDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String imageUrl;
    private List<String> requirements;
    private List<MajorDTO> majors;
    private String website;

    public UniversityDTO(Long id, String name, String description, String location, String imageUrl, List<String> requirements, List<MajorDTO> majors, String website) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.requirements = requirements;
        this.majors = majors;
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
