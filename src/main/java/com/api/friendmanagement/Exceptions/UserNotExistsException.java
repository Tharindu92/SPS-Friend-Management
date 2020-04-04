package com.api.friendmanagement.Exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(String errorMessage){
        super(errorMessage);
    }
}
