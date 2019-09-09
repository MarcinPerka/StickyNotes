package com.archu.stickynotes.config;

import com.archu.stickynotes.note.NoteRepository;
import com.archu.stickynotes.registration.RegistrationConfirmationToken;
import com.archu.stickynotes.registration.RegistrationConfirmationTokenRepository;
import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.role.RoleRepository;
import com.archu.stickynotes.user.User;
import com.archu.stickynotes.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile("prep-prod")
public class PrepProductionConfig {

    @Bean
    public CommandLineRunner init(RegistrationConfirmationTokenRepository registrationConfirmationTokenRepository, NoteRepository noteRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            registrationConfirmationTokenRepository.deleteAll();
            roleRepository.deleteAll();
            userRepository.deleteAll();
            noteRepository.deleteAll();
            
            Set roles = new HashSet();
            Role userRole = new Role();
            userRole.setId("1");
            userRole.setCode("ROLE_USER");
            userRole.setLabel("USER");
            roleRepository.save(userRole);
            roles.add(userRole);
            Role adminRole = new Role();
            adminRole.setId("2");
            adminRole.setCode("ROLE_ADMIN");
            adminRole.setLabel("ADMIN");
            roleRepository.save(adminRole);
            roles.add(adminRole);

            for (Role role : roleRepository.findAll())
                System.out.println(role);

            User user = new User();
            user.setUsername("admin123");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEnabled(true);
            user.setEmail("admin@gmail.com");
            user.setRoles(roles);
            userRepository.save(user);

        };
    }
}
