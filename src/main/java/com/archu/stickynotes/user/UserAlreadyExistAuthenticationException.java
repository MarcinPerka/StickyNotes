package com.archu.stickynotes.user;

public class UserAlreadyExistAuthenticationException extends Exception {

    public UserAlreadyExistAuthenticationException(String message) {
        super(message);
    }
}
