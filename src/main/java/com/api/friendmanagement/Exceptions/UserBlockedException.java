package com.api.friendmanagement.Exceptions;

public class UserBlockedException extends Exception {
    public UserBlockedException(String errorMessage){
        super(errorMessage);
    }
}
