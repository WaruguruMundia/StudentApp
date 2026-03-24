package com.example.studentapp.service;

import com.example.studentapp.model.Student;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    // --- Basic CRUD Operations ---

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // --- Enhanced Functionality (Merged from 1st Version) ---

    /**
     * Searches for students by name.
     * If the query is empty, it returns all students.
     */
    public List<Student> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.findByNameContainingIgnoreCase(query.trim());
    }

    /**
     * Checks if an email is already in use by any student.
     */
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    /**
     * Checks if an email is taken by a DIFFERENT student (used for updates).
     */
    public boolean emailExistsForOther(String email, Long id) {
        Student student = studentRepository.findByEmail(email);
        return student != null && !student.getId().equals(id);
    }
}