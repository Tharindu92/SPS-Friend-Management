package com.api.friendmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column(nullable = false, unique = true)
    private String username;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iduser;

//    @OneToMany(mappedBy = "user")
////    @JoinTable(name = "friendships",
////            joinColumns = @JoinColumn(name = "iduser"),
////            inverseJoinColumns = @JoinColumn(name = "idfriend"))
//    private Set<Friendships> friendsList = new HashSet<Friendships>();;

//    @OneToMany(mappedBy="friend")
//    private Set<Friendships> friendOf = new HashSet<Friendships>();;

    public User(String username) {
        this.username = username;
    }

}
