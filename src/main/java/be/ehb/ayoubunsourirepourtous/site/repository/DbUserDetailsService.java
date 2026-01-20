package be.ehb.ayoubunsourirepourtous.site.security;

import be.ehb.ayoubunsourirepourtous.site.repository.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepo;

    public DbUserDetailsService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(u.getUsername())
                .password(u.getPasswordHash())     // BCrypt stocké en DB
                .roles(u.getRole())                // "ADMIN" => ROLE_ADMIN
                .build();
    }
}
