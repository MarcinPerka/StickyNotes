package com.archu.stickynotes.config;

import com.archu.stickynotes.note.Note;
import com.archu.stickynotes.note.NoteRepository;
import com.archu.stickynotes.registration.RegistrationConfirmationTokenRepository;
import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.role.RoleRepository;
import com.archu.stickynotes.user.User;
import com.archu.stickynotes.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile("development")
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner init(NoteRepository noteRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, RegistrationConfirmationTokenRepository registrationConfirmationTokenRepository) {
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
            user.setEmail("mail@gmail.com");
            user.setRoles(roles);
            userRepository.save(user);

            roles.remove(adminRole);
            User user2 = new User();
            user2.setUsername("test123");
            user2.setPassword(passwordEncoder.encode("test123"));
            user2.setEnabled(true);
            user2.setEmail("qiehakl@gmail.com");
            user2.setRoles(roles);
            userRepository.save(user2);

            for (User user1 : userRepository.findAll())
                System.out.println(user1);

            for(int i = 0;i<10;i++) {
                Note note = new Note();
                note.setTitle("Test");
                note.setUser(user);
                note.setNote("TestTestTestTest");
                noteRepository.save(note);
            }
           // Note note2 = new Note();
         //   note2.setId("2");
        //    note2.setTitle("Test2");
         //   note2.setUser(user);
        //    note2.setNote("Test2Test2Test2Test");
       //     noteRepository.save(note);
        //    noteRepository.save(note2);
        };
    }
}
