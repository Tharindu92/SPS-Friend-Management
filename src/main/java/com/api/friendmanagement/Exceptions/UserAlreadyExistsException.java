package com.api.friendmanagement.Exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
