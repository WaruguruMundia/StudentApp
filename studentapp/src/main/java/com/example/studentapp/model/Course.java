package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 100, message = "Course name must be 3–100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Course code is required")
    @Pattern(regexp = "^[A-Z]{2,5}[0-9]{3,4}$", message = "Code format: 2-5 uppercase letters + 3-4 digits (e.g. CS101)")
    @Column(nullable = false, unique = true)
    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits cannot exceed 6")
    private Integer credits;

    @NotBlank(message = "Instructor name is required")
    private String instructor;

    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations;

    public enum Department {
        COMPUTER_SCIENCE, MATHEMATICS, PHYSICS, CHEMISTRY,
        BIOLOGY, ENGINEERING, BUSINESS, ARTS, HUMANITIES
    }

    // Constructors
    public Course() {}

    public Course(String name, String code, Integer credits, String instructor) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.instructor = instructor;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
}
