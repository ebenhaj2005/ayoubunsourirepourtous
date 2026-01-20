package be.ehb.ayoubunsourirepourtous.site.repository;

import be.ehb.ayoubunsourirepourtous.site.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByRole(String role);
}
