package com.cris.sec.registration.exceptions;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String message){super(message);}

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
