package com.example.studentapp.controller;

import com.example.studentapp.model.Student;
import com.example.studentapp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ── List all students (with optional search) ──────────────────────────────
    @GetMapping
    public String getAllStudents(@RequestParam(required = false) String q, Model model) {
        List<Student> students = (q != null && !q.isBlank())
                ? studentService.search(q)
                : studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("searchQuery", q);
        return "students/list";
    }

    // ── Show add student form ─────────────────────────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("pageTitle", "Add New Student");
        return "students/form";
    }

    // ── Save new student ──────────────────────────────────────────────────────
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              Model model,
                              RedirectAttributes ra) {
        if (student.getEmail() != null && studentService.emailExists(student.getEmail())) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("pageTitle", "Add New Student");
            return "students/form";
        }
        studentService.saveStudent(student);
        ra.addFlashAttribute("successMsg", "Student \"" + student.getFullName() + "\" added successfully!");
        return "redirect:/students";
    }

    // ── Show edit form ────────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("genders", Student.Gender.values());
                    model.addAttribute("pageTitle", "Edit Student");
                    return "students/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Student not found.");
                    return "redirect:/students";
                });
    }

    // ── Update existing student ───────────────────────────────────────────────
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model,
                                RedirectAttributes ra) {
        if (student.getEmail() != null && studentService.emailExistsForOther(student.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered to another student");
        }
        if (result.hasErrors()) {
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("pageTitle", "Edit Student");
            return "students/form";
        }
        student.setId(id);
        studentService.saveStudent(student);
        ra.addFlashAttribute("successMsg", "Student updated successfully!");
        return "redirect:/students";
    }

    // ── View student detail ───────────────────────────────────────────────────
    @GetMapping("/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "students/view";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Student not found.");
                    return "redirect:/students";
                });
    }

    // ── Delete student (POST — safer than GET) ────────────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes ra) {
        studentService.getStudentById(id).ifPresentOrElse(
                s -> {
                    studentService.deleteStudent(id);
                    ra.addFlashAttribute("successMsg", "Student \"" + s.getFullName() + "\" deleted.");
                },
                () -> ra.addFlashAttribute("errorMsg", "Student not found.")
        );
        return "redirect:/students";
    }
}