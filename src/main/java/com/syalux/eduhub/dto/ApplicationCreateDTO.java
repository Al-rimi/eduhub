package com.syalux.eduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationCreateDTO {
    private Long universityId;
    private Long majorId;
    private String applicationData;
}
