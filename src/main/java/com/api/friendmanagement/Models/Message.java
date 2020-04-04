package com.api.friendmanagement.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Message {

    @JsonProperty("success")
    private boolean success;

    private List<String> friends;

    private int count;

    private String message;

    private List<String> recipients;
}
