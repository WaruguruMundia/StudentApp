package com.example.studentapp.repository;

import com.example.studentapp.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT r FROM Registration r JOIN FETCH r.student JOIN FETCH r.course")
    List<Registration> findAllWithDetails();

    List<Registration> findByStudentId(Long studentId);
    List<Registration> findByCourseId(Long courseId);

    @Query("SELECT COUNT(DISTINCT r.student) FROM Registration r WHERE r.course.id = :courseId")
    long countStudentsByCourseId(Long courseId);
}
