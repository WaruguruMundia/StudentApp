package com.example.studentapp.repository;

import com.example.studentapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Student> findByNameContainingIgnoreCase(@Param("query") String query);

    // Supports the emailExists(String email) method
    boolean existsByEmail(String email);

    // Supports the emailExistsForOther(String email, Long id) method
    Student findByEmail(String email);
}