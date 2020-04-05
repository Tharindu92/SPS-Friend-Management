package com.api.friendmanagement.repositories;

import com.api.friendmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepo extends JpaRepository<User, Integer> {

    @Query(value = "select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.iduser)", nativeQuery = true)
    List<User> findFriendsByUsername(String username);

    @Query(value = "select * from (select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.iduser)) as t natural join (select * from users as u where u.iduser in (select idfriend from users as u2, friendships as f where u2.username = ?2 and u2.iduser = f.iduser)) as s where t.iduser = s.iduser", nativeQuery = true)
    List<User> findCommonByUsernames(String user1, String user2);

    List<User> findUserByUsername(String username);

    @Query(value = "select * from users as u where u.iduser in (select f.iduser from users as u2, friendships as f where u2.username = ?1 and u2.iduser = f.idfriend and f.blocked = FALSE )", nativeQuery = true)
    List<User> findSubscribersByUsername(String username);

    @Query(value = "select exists (select * from users where iduser in (select f.iduser from users as u, friendships as f  where u.iduser = f.idfriend and u.username = ?1 and blocked = TRUE) and username = ?2)", nativeQuery = true)
    Integer isUserBlocked(String block, String blockedBy);
}
