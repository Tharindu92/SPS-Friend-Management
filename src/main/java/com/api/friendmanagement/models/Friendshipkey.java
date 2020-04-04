package com.api.friendmanagement.Models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Friendshipkey implements Serializable {

    private int iduser;

    private int idfriend;

    // standard constructors, getters, and setters
    // hashcode and equals implementation
}
