package be.ehb.ayoubunsourirepourtous.site.controller;

import be.ehb.ayoubunsourirepourtous.site.entity.Project;
import be.ehb.ayoubunsourirepourtous.site.entity.ProjectPhoto;
import be.ehb.ayoubunsourirepourtous.site.repository.ProjectPhotoRepository;
import be.ehb.ayoubunsourirepourtous.site.repository.ProjectRepository;
import be.ehb.ayoubunsourirepourtous.site.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProjectRepository projectRepository;
    private final ProjectPhotoRepository photoRepository;
    private final FileStorageService storageService;

    public AdminController(ProjectRepository projectRepository,
                           ProjectPhotoRepository photoRepository,
                           FileStorageService storageService) {
        this.projectRepository = projectRepository;
        this.photoRepository = photoRepository;
        this.storageService = storageService;
    }

    // ✅ Dashboard = liste projets
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "admin/dashboard";
    }

    // ✅ Page liste projets (optionnelle, si tu la gardes)
    @GetMapping("/projects")
    public String adminProjects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "admin/projects";
    }

    // ✅ Form creation
    @GetMapping("/projects/new")
    public String newProjectForm(Model model) {
        Project project = new Project();
        project.setPhotos(new ArrayList<>()); // évite NullPointer si photos == null

        model.addAttribute("project", project);
        model.addAttribute("pageTitle", "Nouveau projet");
        model.addAttribute("formAction", "/admin/projects"); // POST create
        return "admin/project-form";
    }

    // ✅ Create
    @PostMapping("/projects")
    public String createProject(@ModelAttribute Project project,
                                @RequestParam(required = false, name="photoFiles") MultipartFile[] photos) throws Exception {
        if (project.getDate() == null) project.setDate(LocalDate.now());
        projectRepository.save(project);

        if (photos != null) {
            for (MultipartFile file : photos) {
                if (file.isEmpty()) continue;
                String filename = storageService.store(file);

                ProjectPhoto p = new ProjectPhoto();
                p.setFilename(filename);
                p.setProject(project);

                photoRepository.save(p);
                project.getPhotos().add(p);
            }
        }
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @InitBinder("project")
    public void initProjectBinder(WebDataBinder binder) {
        binder.setDisallowedFields("photos");
    }

    // ✅ Form edit
    @GetMapping("/projects/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project project = projectRepository.findById(id).orElseThrow();

        if (project.getPhotos() == null) project.setPhotos(new ArrayList<>());

        model.addAttribute("project", project);
        model.addAttribute("pageTitle", "Modifier le projet");
        model.addAttribute("formAction", "/admin/projects/" + id); // POST update
        return "admin/project-form";
    }

    // ✅ Update
    @PostMapping("/projects/{id}")
    public String updateProject(@PathVariable Long id,
                                @ModelAttribute Project form,
                                @RequestParam(required = false) MultipartFile[] photos) throws Exception {

        Project project = projectRepository.findById(id).orElseThrow();

        project.setTitle(form.getTitle());
        project.setLieu(form.getLieu());
        project.setDescription(form.getDescription());
        project.setDate(form.getDate());

        if (project.getPhotos() == null) project.setPhotos(new ArrayList<>());

        if (photos != null) {
            for (MultipartFile file : photos) {
                if (file == null || file.isEmpty()) continue;

                String filename = storageService.store(file);
                ProjectPhoto p = new ProjectPhoto();
                p.setFilename(filename);
                p.setProject(project);

                photoRepository.save(p);
                project.getPhotos().add(p);
            }
        }

        projectRepository.save(project);
        return "redirect:/admin";
    }

    // ✅ Delete
    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/admin";
    }


    @PostMapping("/projects/{projectId}/photos/{photoId}/delete")
    public String deletePhoto(@PathVariable Long projectId, @PathVariable Long photoId) {
        var project = projectRepository.findById(projectId).orElseThrow();
        var photo = photoRepository.findById(photoId).orElseThrow();

        // sécurité : vérifier que la photo appartient bien au projet
        if (photo.getProject() != null && photo.getProject().getId().equals(projectId)) {
            // supprimer fichier sur disque
            storageService.delete(photo.getFilename());

            // supprimer en DB
            project.getPhotos().remove(photo); // orphanRemoval = true -> ok
            photoRepository.delete(photo);
            projectRepository.save(project);
        }

        return "redirect:/admin/projects/" + projectId + "/edit";
    }
}
