package com.example.studentapp;

import com.example.studentapp.model.*;
import com.example.studentapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private StudentRepository studentRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private RegistrationRepository registrationRepo;

    @Override
    public void run(String... args) {
        if (studentRepo.count() > 0) return; // Don't seed if data exists

        // Students
        Student s1 = new Student("Amara", "Osei", "amara.osei@uni.edu", LocalDate.of(2001, 3, 15));
        s1.setGender(Student.Gender.FEMALE); s1.setPhone("+254712345678"); studentRepo.save(s1);

        Student s2 = new Student("Kwame", "Mensah", "kwame.mensah@uni.edu", LocalDate.of(2000, 7, 22));
        s2.setGender(Student.Gender.MALE); s2.setPhone("+254798765432"); studentRepo.save(s2);

        Student s3 = new Student("Fatima", "Al-Hassan", "fatima.alhassan@uni.edu", LocalDate.of(2002, 1, 10));
        s3.setGender(Student.Gender.FEMALE); studentRepo.save(s3);

        Student s4 = new Student("David", "Kariuki", "david.kariuki@uni.edu", LocalDate.of(2001, 9, 5));
        s4.setGender(Student.Gender.MALE); studentRepo.save(s4);

        Student s5 = new Student("Nora", "Wanjiku", "nora.wanjiku@uni.edu", LocalDate.of(2000, 11, 28));
        s5.setGender(Student.Gender.FEMALE); studentRepo.save(s5);

        // Courses
        Course c1 = new Course("Introduction to Computer Science", "CS101", 3, "Dr. Otieno");
        c1.setDepartment(Course.Department.COMPUTER_SCIENCE);
        c1.setDescription("Foundations of programming and computational thinking.");
        courseRepo.save(c1);

        Course c2 = new Course("Calculus I", "MATH201", 4, "Prof. Njoroge");
        c2.setDepartment(Course.Department.MATHEMATICS);
        c2.setDescription("Limits, derivatives, and integrals.");
        courseRepo.save(c2);

        Course c3 = new Course("Data Structures", "CS202", 3, "Dr. Abubakar");
        c3.setDepartment(Course.Department.COMPUTER_SCIENCE);
        c3.setDescription("Arrays, linked lists, trees, and graphs.");
        courseRepo.save(c3);

        Course c4 = new Course("Physics I", "PHY101", 3, "Dr. Mwangi");
        c4.setDepartment(Course.Department.PHYSICS);
        courseRepo.save(c4);

        Course c5 = new Course("Technical Writing", "ENG105", 2, "Ms. Adeyemi");
        c5.setDepartment(Course.Department.HUMANITIES);
        courseRepo.save(c5);

        // Registrations
        List<Object[]> regs = List.of(
            new Object[]{s1, c1, 88.5, Registration.Status.ACTIVE},
            new Object[]{s1, c2, 92.0, Registration.Status.COMPLETED},
            new Object[]{s2, c1, 75.0, Registration.Status.ACTIVE},
            new Object[]{s2, c3, 81.5, Registration.Status.ACTIVE},
            new Object[]{s3, c2, 95.0, Registration.Status.COMPLETED},
            new Object[]{s3, c5, null, Registration.Status.ACTIVE},
            new Object[]{s4, c1, 68.0, Registration.Status.ACTIVE},
            new Object[]{s4, c4, 73.5, Registration.Status.ACTIVE},
            new Object[]{s5, c3, null, Registration.Status.PENDING},
            new Object[]{s5, c5, 89.0, Registration.Status.COMPLETED}
        );

        for (Object[] r : regs) {
            Registration reg = new Registration((Student) r[0], (Course) r[1]);
            reg.setGrade((Double) r[2]);
            reg.setStatus((Registration.Status) r[3]);
            registrationRepo.save(reg);
        }
    }
}
