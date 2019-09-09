package com.archu.stickynotes.user;

import com.archu.stickynotes.note.Note;
import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(User user) throws UserAlreadyExistAuthenticationException {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistAuthenticationException("User with this username or email already exists.");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByCode("ROLE_USER").get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void enableUser(String id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isPresent()) {
            optUser.get().setEnabled(true);
            userRepository.save(optUser.get());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void enabledUserByAdmin(Boolean enabled, String id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isPresent()) {
            optUser.get().setEnabled(enabled);
            userRepository.save(optUser.get());
        }
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void updateUsername(String username, String id) throws UsernameAlreadyExistsException {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            Optional<User> usernameAvailability = userRepository.findByUsername(username);
            if (!usernameAvailability.isPresent()) {
                userToUpdate.get().setUsername(username);
                userRepository.save(userToUpdate.get());
            } else
                throw new UsernameAlreadyExistsException("In our base we have the user with " + username + " nickname.");
        }
    }

    public void updatePassword(String password, String id) {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            userToUpdate.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(userToUpdate.get());
        }
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void updateEmail(String email, String id) throws EmailAlreadyExistsException {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            if (!userRepository.findByEmail(email).isPresent()) {
                userToUpdate.get().setEmail(email);
                userToUpdate.get().setEnabled(false); //verify email
                userRepository.save(userToUpdate.get());
            } else
                throw new EmailAlreadyExistsException("In our base we have the user with " + email + " email.");
        }
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void updateRoles(Set<Role> roles, String id) {
        if(roles.isEmpty())
            throw new NullPointerException("You didn't set any role to user.");
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            userToUpdate.get().setRoles(roles);
            userRepository.save(userToUpdate.get());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
