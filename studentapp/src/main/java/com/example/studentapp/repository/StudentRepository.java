package com.example.studentapp.repository;
 
import com.example.studentapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
 
    // Search by firstName, lastName, or email
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.email)     LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Student> searchByName(@Param("q") String q);
 
    boolean existsByEmail(String email);
 
    Student findByEmail(String email);
}
 
