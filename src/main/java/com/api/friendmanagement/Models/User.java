package com.api.friendmanagement.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"friends","friendOf"})
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
//    private Set<Friendships> friends = new HashSet<Friendships>();;

//    @OneToMany(mappedBy="friend")
//    private Set<Friendships> friendOf = new HashSet<Friendships>();;

    public User(String username) {
        this.username = username;
    }

}
