package com.archu.stickynotes.passwordReset;

import com.archu.stickynotes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSenderImpl javaMailSender;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSenderImpl javaMailSender) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendPasswordResetEmail(String email, PasswordResetToken token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset your password!");
        mailMessage.setText("To reset your password, please click here : "
                + "http://localhost:8080/reset-password?token=" + token.getPasswordResetToken());
        javaMailSender.send(mailMessage);
    }

    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setPasswordResetToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(30);
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public String verifyPasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByPasswordResetToken(token);

        return passwordResetToken.get().getUser().getId();
    }

}
