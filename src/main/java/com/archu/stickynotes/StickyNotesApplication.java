package com.archu.stickynotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@SpringBootApplication
@EnableMongoAuditing
public class StickyNotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(StickyNotesApplication.class, args);
    }

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setUsername("ecce5bad6b06b8");
        javaMailSender.setPassword("9f52b1f848c22c");
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setProtocol("smtp");
        javaMailSender.setPort(587);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.quitwait", false);
        javaMailProperties.put("mail.smtp.socketFactory.class", javax.net.ssl.SSLSocketFactory.class);
        javaMailProperties.put("mail.smtp.socketFactory.fallback", false);

        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

}
