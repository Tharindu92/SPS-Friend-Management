package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GeneralMessage extends AbstractMessage{

    public GeneralMessage(boolean success) {
        this.setSuccess(success);
    }
}
