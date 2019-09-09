package com.archu.stickynotes.user;


import com.archu.stickynotes.registration.RegistrationConfirmationToken;
import com.archu.stickynotes.registration.RegistrationService;
import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.role.RoleRepository;
import com.archu.stickynotes.role.WrapperRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final RegistrationService registrationService;

    @Autowired
    public UserController(UserService userService, RegistrationService registrationService, RoleRepository roleRepository) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal User currentUser, Model model) {
        Optional<User> optionalUser = userService.getUserById(currentUser.getId());
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
        return "user";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id, Model model) {
        Optional<User> optionalUser = userService.getUserById(id);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
        return "userAdmin";
    }

    @GetMapping("/users/page/{page}")
    public String getUsers(Model model, @PathVariable int page) {
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<User> users = userService.getAllUsers(pageable);
        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("users", users.getContent());
        return "users";
    }

    @GetMapping("/users/page/{page}/{sortingItem}/{sortingDirection}")
    public String getAllSortedUsers(Model model, @PathVariable int page, @PathVariable String sortingItem, @PathVariable String sortingDirection) {
        PageRequest pageable;
        if (sortingDirection.equals("desc")) {
            pageable = PageRequest.of(page - 1, 5, Sort.by(sortingItem).descending());
        } else {
            pageable = PageRequest.of(page - 1, 5, Sort.by(sortingItem).ascending());
        }
        Page<User> users = userService.getAllUsers(pageable);
        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSortingItem", sortingItem);
        model.addAttribute("currentSortingDirection", sortingDirection);
        model.addAttribute("users", users.getContent());
        return "usersSorted";
    }

    @Transactional
    @PatchMapping("/user/{id}/update/email")
    public String updateEmail(@PathVariable String id, @RequestParam String email, RedirectAttributes redirectAttributes) throws EmailAlreadyExistsException {
        userService.updateEmail(email, id);
        RegistrationConfirmationToken token = registrationService.createRegistrationConfirmationToken(userService.getUserById(id).get());
        registrationService.sendVerificationEmail(email, token);
        redirectAttributes.addFlashAttribute("message", "The email update was successful.");
        return "redirect:/user/{id}";
    }

    @PatchMapping("/user/{id}/update/password")
    public String updatePassword(@PathVariable String id, @RequestParam String password, RedirectAttributes redirectAttributes) {
        userService.updatePassword(password, id);
        redirectAttributes.addFlashAttribute("message", "The password update was successful.");
        return "redirect:/user/{id}";
    }

    @Transactional
    @PatchMapping("/user/{id}/update/username")
    public String updateUsername(@RequestParam String username, @PathVariable String id, RedirectAttributes redirectAttributes) throws UsernameAlreadyExistsException {
        userService.updateUsername(username, id);
        redirectAttributes.addFlashAttribute("message", "The username update was successful.");
        return "redirect:/user/{id}";

    }

    @Transactional
    @PatchMapping("/user/{id}/update/roles")
    public String updateRoles(@PathVariable String id, WrapperRole wrapperRole, RedirectAttributes redirectAttributes) {
        if(wrapperRole.getRoles() == null)
            throw new NullPointerException("You didn't choose any role");
        Set<Role> roleSet = new HashSet<>(wrapperRole.getRoles());
        userService.updateRoles(roleSet, id);
        redirectAttributes.addFlashAttribute("message", "The role update was successful.");
        return "redirect:/user/{id}";
    }

    @PatchMapping("/user/{id}/update/enabled")
    public String enabledUser(@PathVariable String id, @RequestParam(defaultValue = "false") Boolean enabled, RedirectAttributes redirectAttributes) {
        userService.enabledUserByAdmin(enabled, id);
        redirectAttributes.addFlashAttribute("message", "The availability update was successful.");
        return "redirect:/user/{id}";
    }


    @DeleteMapping("/user/{id}")
    public String deleteUser(@AuthenticationPrincipal User currentUser, @PathVariable String id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        if (id.equals(currentUser.getId()))
            return "redirect:/logout";
        redirectAttributes.addFlashAttribute("message", "User with id "+id+" has been deleted.");
        return "redirect:/users/page/1";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable String id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        if (id.equals(currentUser.getId()))
            return "redirect:/logout";
        redirectAttributes.addFlashAttribute("message", "User with id "+id+" has been deleted.");
        return "users";
    }

    @Transactional
    @PatchMapping("/user/update/email")
    public String updateEmail(@AuthenticationPrincipal User currentUser, @RequestParam String email, RedirectAttributes redirectAttributes) throws EmailAlreadyExistsException {
        userService.updateEmail(email, currentUser.getId());
        RegistrationConfirmationToken token = registrationService.createRegistrationConfirmationToken(currentUser);
        registrationService.sendVerificationChangedEmail(email, token);
        redirectAttributes.addFlashAttribute("message", "The email update was successful.");
        return "redirect:/user";
    }

    @PatchMapping("/user/update/password")
    public String updatePassword(@AuthenticationPrincipal User currentUser, @RequestParam String password, RedirectAttributes redirectAttributes) {
        userService.updatePassword(password, currentUser.getId());
        redirectAttributes.addFlashAttribute("message", "The password update was successful.");
        return "redirect:/user";
    }

    @Transactional
    @PatchMapping("/user/update/username")
    public String updateUsername(@RequestParam String username, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) throws UsernameAlreadyExistsException {
        userService.updateUsername(username, currentUser.getId());
        redirectAttributes.addFlashAttribute("message", "The username update was successful.");
        return "redirect:/user";

    }

    @DeleteMapping("/user")
    public String deleteUser(@AuthenticationPrincipal User currentUser) {
        userService.deleteUser(currentUser.getId());
        return "redirect:/logout";
    }
}
