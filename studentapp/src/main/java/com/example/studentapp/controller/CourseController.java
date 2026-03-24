package com.example.studentapp.controller;

import com.example.studentapp.model.Course;
import com.example.studentapp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        List<Course> courses = (q != null && !q.isBlank())
                ? courseService.search(q)
                : courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("searchQuery", q);
        return "courses/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", Course.Department.values());
        model.addAttribute("pageTitle", "Add New Course");
        return "courses/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute Course course, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (course.getCode() != null && courseService.codeExists(course.getCode())) {
            result.rejectValue("code", "duplicate", "This course code already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("departments", Course.Department.values());
            model.addAttribute("pageTitle", "Add New Course");
            return "courses/form";
        }
        courseService.save(course);
        ra.addFlashAttribute("successMsg", "Course \"" + course.getName() + "\" created successfully!");
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return courseService.findById(id).map(course -> {
            model.addAttribute("course", course);
            model.addAttribute("departments", Course.Department.values());
            model.addAttribute("pageTitle", "Edit Course");
            return "courses/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Course not found.");
            return "redirect:/courses";
        });
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Course course,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (course.getCode() != null && courseService.codeExistsForOther(course.getCode(), id)) {
            result.rejectValue("code", "duplicate", "This course code is already used by another course");
        }
        if (result.hasErrors()) {
            model.addAttribute("departments", Course.Department.values());
            model.addAttribute("pageTitle", "Edit Course");
            return "courses/form";
        }
        course.setId(id);
        courseService.save(course);
        ra.addFlashAttribute("successMsg", "Course updated successfully!");
        return "redirect:/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        courseService.findById(id).ifPresentOrElse(
            c -> {
                courseService.deleteById(id);
                ra.addFlashAttribute("successMsg", "Course \"" + c.getName() + "\" deleted.");
            },
            () -> ra.addFlashAttribute("errorMsg", "Course not found.")
        );
        return "redirect:/courses";
    }
}
