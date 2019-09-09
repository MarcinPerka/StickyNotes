package com.archu.stickynotes.registration;

import com.archu.stickynotes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    private final JavaMailSenderImpl javaMailSender;
    private final RegistrationConfirmationTokenRepository registrationConfirmationTokenRepository;

    @Autowired
    public RegistrationService(JavaMailSenderImpl javaMailSender, RegistrationConfirmationTokenRepository registrationConfirmationTokenRepository) {
        this.javaMailSender = javaMailSender;
        this.registrationConfirmationTokenRepository = registrationConfirmationTokenRepository;
    }

    @Async
    public void sendVerificationEmail(String email, RegistrationConfirmationToken token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/confirm-account?token=" + token.getRegistrationConfirmationToken());
        javaMailSender.send(mailMessage);
    }

    public RegistrationConfirmationToken createRegistrationConfirmationToken(User user) {
        RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(user);
        return registrationConfirmationTokenRepository.save(registrationConfirmationToken);
    }

    public Optional<String> verifyRegistrationConfirmationToken(String token) {
        Optional<RegistrationConfirmationToken> registrationConfirmationToken = registrationConfirmationTokenRepository.findByRegistrationConfirmationToken(token);

        return registrationConfirmationToken.map(registrationConfirmationToken1 -> Optional.ofNullable(registrationConfirmationToken1.getUser().getId())).orElse(null);
    }

    @Async
    public void sendVerificationChangedEmail(String email, RegistrationConfirmationToken token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Did you change email?");
        mailMessage.setText("To confirm email change, please click here : "
                + "http://localhost:8080/confirm-account?token=" + token.getRegistrationConfirmationToken());
        javaMailSender.send(mailMessage);
    }
}
