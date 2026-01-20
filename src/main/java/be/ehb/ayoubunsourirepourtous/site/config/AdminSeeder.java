package be.ehb.ayoubunsourirepourtous.site.config;

import be.ehb.ayoubunsourirepourtous.site.entity.AppUser;
import be.ehb.ayoubunsourirepourtous.site.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:Ayoub2024!}")
    private String adminPassword; // en clair -> hashé au seed

    public AdminSeeder(AppUserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // si aucun ADMIN en base -> on le crée
        if (!userRepo.existsByRole("ADMIN")) {
            String hash = encoder.encode(adminPassword);
            userRepo.save(new AppUser(adminUsername, hash, "ADMIN"));
            System.out.println("✅ Admin créé en DB: " + adminUsername);
        }
    }
}
