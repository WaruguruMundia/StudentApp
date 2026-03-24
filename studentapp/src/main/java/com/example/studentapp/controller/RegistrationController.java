package com.example.studentapp.controller;

import com.example.studentapp.model.Registration;
import com.example.studentapp.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired private RegistrationService registrationService;
    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("registrations", registrationService.findAll());
        return "registrations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("registration", new Registration());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("statuses", Registration.Status.values());
        return "registrations/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute Registration registration, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (registration.getStudent() != null && registration.getCourse() != null) {
            if (registrationService.alreadyRegistered(
                    registration.getStudent().getId(), registration.getCourse().getId())) {
                result.rejectValue("course", "duplicate",
                        "This student is already registered for that course");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.findAll());
            model.addAttribute("statuses", Registration.Status.values());
            return "registrations/form";
        }
        registrationService.save(registration);
        ra.addFlashAttribute("successMsg", "Registration created successfully!");
        return "redirect:/registrations";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return registrationService.findById(id).map(reg -> {
            model.addAttribute("registration", reg);
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.findAll());
            model.addAttribute("statuses", Registration.Status.values());
            return "registrations/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("errorMsg", "Registration not found.");
            return "redirect:/registrations";
        });
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Registration registration,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.findAll());
            model.addAttribute("statuses", Registration.Status.values());
            return "registrations/form";
        }
        registration.setId(id);
        registrationService.save(registration);
        ra.addFlashAttribute("successMsg", "Registration updated successfully!");
        return "redirect:/registrations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        registrationService.findById(id).ifPresentOrElse(
            r -> {
                registrationService.deleteById(id);
                ra.addFlashAttribute("successMsg", "Registration removed successfully.");
            },
            () -> ra.addFlashAttribute("errorMsg", "Registration not found.")
        );
        return "redirect:/registrations";
    }
}
