package com.api.friendmanagement.Repositories;

import com.api.friendmanagement.Models.Friendships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepo extends JpaRepository<Friendships, Integer> {
    @Query(value = "select * from friendships where iduser = ?1 and idfriend = ?2", nativeQuery = true)
    List<Friendships> findFriendshipByiduser(Integer iduser, Integer idfriend);


}
