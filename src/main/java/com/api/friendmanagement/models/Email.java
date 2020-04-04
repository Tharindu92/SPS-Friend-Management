package com.api.friendmanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Email {

    @JsonProperty("email")
    private String emailId;
}
