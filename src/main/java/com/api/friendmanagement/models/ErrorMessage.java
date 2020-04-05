package com.api.friendmanagement.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage extends AbstractMessage {
    private String errorMessage;

    public ErrorMessage(boolean success, String errorMessage) {
        this.errorMessage = errorMessage;
        this.setSuccess(success);
    }
}
