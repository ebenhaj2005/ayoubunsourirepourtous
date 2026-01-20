package be.ehb.ayoubunsourirepourtous.site.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_photos")
public class ProjectPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ProjectPhoto() {}

    public ProjectPhoto(String filename, Project project) {
        this.filename = filename;
        this.project = project;
    }

    public Long getId() { return id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}
