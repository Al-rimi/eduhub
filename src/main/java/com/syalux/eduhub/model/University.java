package com.syalux.eduhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "universities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "university_requirements", joinColumns = @JoinColumn(name = "university_id"))
    @Column(name = "requirement", columnDefinition = "TEXT")
    private List<String> requirements;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Major> majors;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_admin_user_id")
    private User facilityAdmin;

    @ManyToMany(mappedBy = "assignedUniversities")
    private Set<User> assignedStaff;

    public University(String name, String description, String location, String imageUrl, List<String> requirements) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.requirements = requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
