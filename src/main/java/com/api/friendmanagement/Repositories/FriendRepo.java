package com.api.friendmanagement.Repositories;

import com.api.friendmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepo extends JpaRepository<User, Integer> {
//    @Query("SELECT '*' from users")
//    List<User> findAll();
    User findUserByUsername(String userName);
}
