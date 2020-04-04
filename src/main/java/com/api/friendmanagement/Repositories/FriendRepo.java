package com.api.friendmanagement.Repositories;

import com.api.friendmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepo extends JpaRepository<User, Integer> {
//    @Query("SELECT '*' from users")
//    List<User> findAll();
    @Query(value = "select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.iduser)", nativeQuery = true)
    List<User> findFriendsByUsername(String username);

    List<User> findUserByUsername(String username);

//    @Query("SELECT u2 from User u2 where u2 MEMBER of u1.friends")
//    List<User> findFriendsByUsername(@Param("u1") User u1);
}
