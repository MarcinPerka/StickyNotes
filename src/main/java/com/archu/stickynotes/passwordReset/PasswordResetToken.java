package com.archu.stickynotes.passwordReset;

import com.archu.stickynotes.user.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Document
@Data
public class PasswordResetToken {

    @Id
    private String id;

    @NotNull
    private String passwordResetToken;

    @DBRef
    @NotNull
    private User user;

    @NotNull
    private Date expirationDate;


    public void setExpiryDate(int minutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.expirationDate = now.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expirationDate);
    }

}
