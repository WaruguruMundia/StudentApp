package com.example.studentapp.service;

import com.example.studentapp.model.Registration;
import com.example.studentapp.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    public List<Registration> findAllWithDetails() {
        return registrationRepository.findAllWithDetails();
    }

    public Optional<Registration> findById(Long id) {
        return registrationRepository.findById(id);
    }

    public Registration save(Registration registration) {
        return registrationRepository.save(registration);
    }

    public void deleteById(Long id) {
        registrationRepository.deleteById(id);
    }

    public boolean alreadyRegistered(Long studentId, Long courseId) {
        return registrationRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    public List<Registration> findByStudentId(Long studentId) {
        return registrationRepository.findByStudentId(studentId);
    }

    public List<Registration> findByCourseId(Long courseId) {
        return registrationRepository.findByCourseId(courseId);
    }
}
