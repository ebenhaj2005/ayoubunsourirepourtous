package be.ehb.ayoubunsourirepourtous.site.controller;

import be.ehb.ayoubunsourirepourtous.site.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Service
public class PublicController {

    private final ProjectRepository projectRepository;

    public PublicController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "public/home";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "projects";
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        var opt = projectRepository.findById(id);

        if (opt.isEmpty()) {
            return "errors/404"; // ou "redirect:/projects"
        }

        model.addAttribute("project", opt.get());
        return "project-detail";
    }

    @GetMapping("/contact")
    public String contact() {
        return "public/contact";
    }
}
