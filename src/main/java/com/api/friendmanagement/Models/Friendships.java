package com.api.friendmanagement.Models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "FRIENDSHIPS")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Friendships {
//    @EmbeddedId
//    private Friendshipkey id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idfriendship;


//    @ManyToOne
//    @MapsId("iduser")
//    @JoinColumn(name = "iduser")
    private Integer iduser;

//    @ManyToOne
//    @MapsId("iduser")
//    @JoinColumn(name = "idfriend")
    private Integer idfriend;

    @Column(columnDefinition = "BIT default 0")
    private boolean blocked;
}
