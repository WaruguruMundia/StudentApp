package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    // ── Identity ──────────────────────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Name (split for sorting/display; compatibility shim included below) ───
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be 2–50 characters")
    @Column(nullable = false)
    private String lastName;

    // ── Contact ───────────────────────────────────────────────────────────────
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Please enter a valid phone number")
    private String phone;

    // ── Demographics ──────────────────────────────────────────────────────────
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // ── Academic info (from basic file — kept, domain-relevant) ──────────────
    @Size(max = 100, message = "Program name cannot exceed 100 characters")
    private String program;

    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 6, message = "Year cannot exceed 6")
    private Integer year;

    // ── Relationships ─────────────────────────────────────────────────────────
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations;

    // ── Enums ─────────────────────────────────────────────────────────────────
    public enum Gender { MALE, FEMALE, OTHER }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    /** Full constructor for programmatic creation (e.g. DataSeeder) */
    public Student(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    /** Extended constructor including academic info */
    public Student(String firstName, String lastName, String email,
                   LocalDate dateOfBirth, String program, Integer year) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.program = program;
        this.year = year;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }

    // ── Helper methods ────────────────────────────────────────────────────────

    /** Primary display name — used throughout templates and controllers */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Backward-compatibility shim for any code that calls getName().
     * Delegates to getFullName() so old service/template code keeps working
     * without changes.
     */
    public String getName() {
        return getFullName();
    }

    /**
     * Backward-compatibility setter — splits a "First Last" string into
     * firstName and lastName so legacy code calling setName() still works.
     * If the string has no space, the whole value goes into firstName.
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) return;
        int spaceIndex = name.trim().indexOf(' ');
        if (spaceIndex == -1) {
            this.firstName = name.trim();
            this.lastName = "";
        } else {
            this.firstName = name.trim().substring(0, spaceIndex);
            this.lastName = name.trim().substring(spaceIndex + 1);
        }
    }

    /** Formatted year display, e.g. "Year 2" or "N/A" */
    public String getYearDisplay() {
        return year != null ? "Year " + year : "N/A";
    }
}