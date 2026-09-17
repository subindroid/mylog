package com.marumiru.mylog.service;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
    };

    public UserNotFoundException(String message) {
        super(message);
    };
}
