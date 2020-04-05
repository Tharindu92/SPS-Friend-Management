package com.api.friendmanagement.exceptions;

public class UserBlockedException extends Exception {
    public UserBlockedException(String friend, String friendWith){
        super(friend + " is blocked by " + friendWith);
    }
}
