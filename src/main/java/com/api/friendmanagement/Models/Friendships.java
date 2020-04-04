package com.api.friendmanagement.Models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Friendships {
    @EmbeddedId
    private Friendshipkey id;

    @ManyToOne
    @MapsId("iduser")
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne
    @MapsId("iduser")
    @JoinColumn(name = "idfriend")
    private User friend;

    @Column(columnDefinition = "BIT default 0")
    private boolean blocked;
}
