package com.api.friendmanagement.exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(String username){
        super("Requested user is not exists " + username);
    }
}
