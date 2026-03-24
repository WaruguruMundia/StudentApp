package com.example.studentapp.repository;

import com.example.studentapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Supports the search(String query) method
    List<Student> findByNameContainingIgnoreCase(String name);

    // Supports the emailExists(String email) method
    boolean existsByEmail(String email);

    // Supports the emailExistsForOther(String email, Long id) method
    Student findByEmail(String email);
}