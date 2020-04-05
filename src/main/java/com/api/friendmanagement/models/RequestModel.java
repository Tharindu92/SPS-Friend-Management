package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel {

    @JsonProperty(value = "requestor")
    private String requester;
    private String target;
}
