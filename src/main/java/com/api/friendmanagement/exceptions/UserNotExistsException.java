package com.api.friendmanagement.exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(String errorMessage){
        super(errorMessage);
    }
}
