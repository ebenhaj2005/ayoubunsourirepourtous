package be.ehb.ayoubunsourirepourtous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("be.ehb.ayoubunsourirepourtous.site.entity")
@EnableJpaRepositories("be.ehb.ayoubunsourirepourtous.site.repository")

public class AyoubunsourirepourtousApplication {
    public static void main(String[] args) {
        SpringApplication.run(AyoubunsourirepourtousApplication.class, args);
    }
}
