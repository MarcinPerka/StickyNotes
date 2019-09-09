package com.archu.stickynotes.registration;

import com.archu.stickynotes.captcha.CaptchaService;
import com.archu.stickynotes.captcha.ReCaptchaInvalidException;
import com.archu.stickynotes.captcha.ReCaptchaUnavailableException;
import com.archu.stickynotes.user.User;
import com.archu.stickynotes.user.UserAlreadyExistAuthenticationException;
import com.archu.stickynotes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final RegistrationService registrationService;
    private final CaptchaService captchaService;

    @Autowired
    public RegistrationController(UserService userService, RegistrationService registrationService, CaptchaService captchaService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.captchaService = captchaService;
    }

    @Transactional
    @PostMapping("/signup")
    public String createUser(User user, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ReCaptchaUnavailableException, ReCaptchaInvalidException, UserAlreadyExistAuthenticationException {
        String response = request.getParameter("g-recaptcha-response");
        captchaService.processResponse(response);
        userService.createUser(user);
        RegistrationConfirmationToken token = registrationService.createRegistrationConfirmationToken(user);
        registrationService.sendVerificationEmail(user.getEmail(), token);
        redirectAttributes.addFlashAttribute("signupMessage", "Successful registration");
        return "redirect:/";
    }


    @RequestMapping(value = "confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        Optional<String> userId = registrationService.verifyRegistrationConfirmationToken(token);
        userId.ifPresent(userService::enableUser);
        redirectAttributes.addFlashAttribute("message", "Successful activation of account. Now you can log in to your account.");
        return "redirect:/";
    }
}
