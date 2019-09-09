package com.archu.stickynotes.passwordReset;


import com.archu.stickynotes.user.User;
import com.archu.stickynotes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @Transactional
    @PostMapping("/forgot-password")
    public String processForgottenPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            PasswordResetToken token = passwordResetService.createPasswordResetToken(optionalUser.get());
            passwordResetService.sendPasswordResetEmail(optionalUser.get().getEmail(), token);
            redirectAttributes.addFlashAttribute("message", "Link to reset password has been sent to provided email.");
        }
        return "redirect:/";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model, @RequestParam String token) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PatchMapping("/reset-password")
    @Transactional
    public String resetPassword(@ModelAttribute("passwordResetDto") PasswordResetDto passwordResetDto, RedirectAttributes redirectAttributes) {
        String userId = passwordResetService.verifyPasswordResetToken(passwordResetDto.getToken());
        userService.updatePassword(passwordResetDto.getPassword(), userId);
        redirectAttributes.addFlashAttribute("message", "Password has been reset. Now you can log in to your account.");
        return "redirect:/";
    }

}

