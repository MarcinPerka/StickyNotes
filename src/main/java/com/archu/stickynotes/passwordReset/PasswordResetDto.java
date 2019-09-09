package com.archu.stickynotes.passwordReset;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordResetDto {

    @NotEmpty
    private String password;

    @NotEmpty
    private String token;
}
