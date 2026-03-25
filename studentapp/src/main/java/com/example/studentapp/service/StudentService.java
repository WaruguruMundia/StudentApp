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

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /** Alias used by RegistrationController and other callers */
    public List<Student> findAll() {
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

    /** Search by first name, last name, or email */
    public List<Student> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchByName(query.trim());
    }

    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    public boolean emailExistsForOther(String email, Long id) {
        Student existing = studentRepository.findByEmail(email);
        return existing != null && !existing.getId().equals(id);
    }
}
