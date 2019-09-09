package com.archu.stickynotes.captcha;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CaptchaSettings {

    @Value("${google.recaptcha.key.site}")
    private String site;

    @Value("${google.recaptcha.key.secret}")
    private String secret;

}