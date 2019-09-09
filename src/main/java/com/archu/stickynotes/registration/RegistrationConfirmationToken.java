package com.archu.stickynotes.registration;


import com.archu.stickynotes.user.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Document
@Data
public class RegistrationConfirmationToken {

    @Id
    private String id;

    @NotNull
    private String registrationConfirmationToken;

    @NotNull
    private Date createdDate;

    @DBRef
    @NotNull
    private User user;

    public RegistrationConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        registrationConfirmationToken = UUID.randomUUID().toString();
    }
}