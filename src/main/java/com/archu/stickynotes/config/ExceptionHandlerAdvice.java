package com.archu.stickynotes.config;

import com.archu.stickynotes.user.EmailAlreadyExistsException;
import com.archu.stickynotes.user.UserAlreadyExistAuthenticationException;
import com.archu.stickynotes.user.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
    public ModelAndView handleException(UserAlreadyExistAuthenticationException e) {
        ModelAndView model = new ModelAndView("customError");
        model.addObject("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addObject("status", HttpStatus.FORBIDDEN.value());
        model.addObject("message", e.getMessage());
        return model;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleException(EmailAlreadyExistsException e) {
        ModelAndView model = new ModelAndView("customError");
        model.addObject("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addObject("status", HttpStatus.FORBIDDEN.value());
        model.addObject("message", e.getMessage());
        return model;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ModelAndView handleException(UsernameAlreadyExistsException e) {
        ModelAndView model = new ModelAndView("customError");
        model.addObject("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addObject("status", HttpStatus.FORBIDDEN.value());
        model.addObject("message", e.getMessage());
        return model;
    }


}

