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

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "friendships",
            joinColumns = @JoinColumn(name = "iduser"),
            inverseJoinColumns = @JoinColumn(name = "idfriend"))
    private Set<User> friends = new HashSet<User>();;

    @ManyToMany(mappedBy="friends")
    private Set<User> friendOf = new HashSet<User>();;

}
