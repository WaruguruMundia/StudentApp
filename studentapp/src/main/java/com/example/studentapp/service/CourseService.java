package com.example.studentapp.service;

import com.example.studentapp.model.Course;
import com.example.studentapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return courseRepository.search(query.trim());
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean codeExists(String code) {
        return courseRepository.existsByCode(code);
    }

    public boolean codeExistsForOther(String code, Long id) {
        return courseRepository.existsByCodeAndIdNot(code, id);
    }
}
