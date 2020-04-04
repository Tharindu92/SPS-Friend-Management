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

    @Query(value = "select * from (select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.iduser)) as t natural join (select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?2 and u2.iduser = f.iduser)) as s where t.iduser = s.iduser", nativeQuery = true)
    List<User> findCommonByUsernames(String user1, String user2);

    List<User> findUserByUsername(String username);

    @Query(value = "select * from users as u where u.iduser in (select f.iduser from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.idfriend and f.blocked = FALSE )", nativeQuery = true)
    List<User> findSubscribersByUsername(String username);

//    @Query("SELECT u2 from User u2 where u2 MEMBER of u1.friends")
//    List<User> findFriendsByUsername(@Param("u1") User u1);
}
