package be.ehb.ayoubunsourirepourtous.site.controller;

import be.ehb.ayoubunsourirepourtous.site.repository.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SiteController {

    private final ProjectRepository projectRepository;

    public SiteController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "projects";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        var projectOpt = projectRepository.findById(id);

        if (projectOpt.isEmpty()) {
            return "errors/404"; // ou "project-not-found"
        }

        model.addAttribute("project", projectOpt.get());
        return "project-details";
    }
}
