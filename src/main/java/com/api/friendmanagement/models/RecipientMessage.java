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
public class RecipientMessage extends AbstractMessage {

    private List<String> recipients;

    public RecipientMessage(boolean success, List<String> recipients) {
        this.recipients = recipients;
        this.setSuccess(success);
    }

    public RecipientMessage(boolean success) {
        this.setSuccess(success);
    }
}
