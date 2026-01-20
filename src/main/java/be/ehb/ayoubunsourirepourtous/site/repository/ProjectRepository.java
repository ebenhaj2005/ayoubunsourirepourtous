package be.ehb.ayoubunsourirepourtous.site.repository;

import be.ehb.ayoubunsourirepourtous.site.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
