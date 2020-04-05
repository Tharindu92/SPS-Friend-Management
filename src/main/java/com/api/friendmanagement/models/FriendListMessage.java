package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FriendListMessage extends AbstractMessage {

    private List<String> friends;

    private int count;

    @JsonProperty(value = "success", required = true)
    private boolean success;

    public FriendListMessage(boolean success, List<String> friends, int count) {
        this.count = count;
        this.friends = friends;
        this.setSuccess(success);
    }

    public FriendListMessage(boolean success) {
        this.setSuccess(success);
    }

//
//    @JsonProperty("message")
//    private String messageText;
//
//    private List<String> recipients;
}
