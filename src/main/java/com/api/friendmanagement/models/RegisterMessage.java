package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RegisterMessage extends AbstractMessage {

    private String message;

    public RegisterMessage(boolean success, String message) {
        this.message = message;
        this.setSuccess(success);
    }
}
