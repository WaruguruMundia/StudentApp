package com.example.studentapp.service;

import com.example.studentapp.model.*;
import com.example.studentapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DashboardService {

    @Autowired private StudentRepository studentRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private RegistrationRepository registrationRepo;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long totalStudents = studentRepo.count();
        long totalCourses = courseRepo.count();
        long totalRegistrations = registrationRepo.count();

        // Active registrations
        long activeRegs = registrationRepo.findAllWithDetails().stream()
                .filter(r -> r.getStatus() == Registration.Status.ACTIVE)
                .count();

        // Avg grade across all graded registrations
        OptionalDouble avgGrade = registrationRepo.findAllWithDetails().stream()
                .filter(r -> r.getGrade() != null)
                .mapToDouble(Registration::getGrade)
                .average();

        stats.put("totalStudents", totalStudents);
        stats.put("totalCourses", totalCourses);
        stats.put("totalRegistrations", totalRegistrations);
        stats.put("activeRegistrations", activeRegs);
        stats.put("avgGrade", avgGrade.isPresent() ? String.format("%.1f", avgGrade.getAsDouble()) : "N/A");

        // Recent registrations (last 5)
        List<Registration> recent = registrationRepo.findAllWithDetails();
        recent.sort(Comparator.comparing(Registration::getRegistrationDate).reversed());
        stats.put("recentRegistrations", recent.stream().limit(5).toList());

        // Top courses by enrollment
        List<Course> courses = courseRepo.findAll();
        courses.sort((a, b) -> {
            long ca = registrationRepo.findByCourseId(a.getId()).size();
            long cb = registrationRepo.findByCourseId(b.getId()).size();
            return Long.compare(cb, ca);
        });
        stats.put("topCourses", courses.stream().limit(4).toList());

        // Gender distribution
        Map<String, Long> genderMap = new LinkedHashMap<>();
        for (Student.Gender g : Student.Gender.values()) {
            long count = studentRepo.findAll().stream()
                    .filter(s -> g.equals(s.getGender())).count();
            if (count > 0) genderMap.put(g.name(), count);
        }
        stats.put("genderDistribution", genderMap);

        return stats;
    }
}
