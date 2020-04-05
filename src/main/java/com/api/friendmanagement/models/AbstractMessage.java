package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class AbstractMessage {
    @JsonProperty(value = "success", required = true)
    private boolean success;
}
