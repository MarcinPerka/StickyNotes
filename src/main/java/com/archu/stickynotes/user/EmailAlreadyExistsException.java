package com.archu.stickynotes.user;


public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
