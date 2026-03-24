package com.example.studentapp.repository;

import com.example.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "OR LOWER(c.code) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "OR LOWER(c.instructor) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Course> search(String q);
}
