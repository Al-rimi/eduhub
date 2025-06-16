package com.syalux.eduhub.dto;

import com.syalux.eduhub.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApplicationDTO {
    private Long id;
    private Long studentId;
    private String studentUsername;
    private Long universityId;
    private String universityName;
    private Long majorId;
    private String majorName;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String applicationData;

    public ApplicationDTO(Long id, Long studentId, String studentUsername, Long universityId, String universityName, Long majorId, String majorName, ApplicationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, String applicationData) {
        this.id = id;
        this.studentId = studentId;
        this.studentUsername = studentUsername;
        this.universityId = universityId;
        this.universityName = universityName;
        this.majorId = majorId;
        this.majorName = majorName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.applicationData = applicationData;
    }
}
