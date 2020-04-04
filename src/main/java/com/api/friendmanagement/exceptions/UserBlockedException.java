package com.api.friendmanagement.exceptions;

public class UserBlockedException extends Exception {
    public UserBlockedException(String errorMessage){
        super(errorMessage);
    }
}
