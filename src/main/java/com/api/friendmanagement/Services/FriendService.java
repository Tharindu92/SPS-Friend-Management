package com.api.friendmanagement.Services;

//import com.microservices.signin.Repositories.UserRepo;
//import com.microservices.signin.Models.Message;
//import com.microservices.signin.Models.User;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import com.api.friendmanagement.Models.*;
import com.api.friendmanagement.Repositories.FriendRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Repository
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

//    private static final Logger LOGGER= LogManager.getLogger(UserAuthService.class);

    public Message addFriends(Friends friends){
        List<User> userList1 = friendRepo.findUserByUsername(friends.getFriends().get(0));
        List<User> userList2= friendRepo.findUserByUsername(friends.getFriends().get(1));
        //System.out.println(user1 + "     "+ user2);
        User user1, user2;
        if(userList1.size() == 0)
            user1 = new User(friends.getFriends().get(0));
        else
            user1 = userList1.get(0);
        if(userList2.size() == 0)
            user2 = new User(friends.getFriends().get(1));
        else
            user2 = userList2.get(0);

        Friendships friendships = new Friendships();
        friendships.setUser(user1);
        friendships.setFriend(user2);
        friendships.setBlocked(false);

        user1.getFriends().add(friendships);

        Friendships friendships2 = new Friendships();
        friendships2.setUser(user2);
        friendships2.setFriend(user1);
        friendships2.setBlocked(false);

        user1.getFriends().add(friendships);
        user1.getFriendOf().add(friendships2);
        user2.getFriendOf().add(friendships2);
        user2.getFriends().add(friendships);



        friendRepo.save(user1);
        friendRepo.save(user2);

//        blockFriendbyUsername(user1.getUsername(), user2.getUsername());
        Message message = new Message();
        message.setSuccess(true);
        return message;
    }

    public Message getFriendsByUserName(Email email) {
        Message message = new Message();
        List<String> friendNames = new ArrayList<>();
        List<User> friends = friendRepo.findFriendsByUsername(email.getEmail());
        for(User friend : friends){
            friendNames.add(friend.getUsername());
        }
        message.setSuccess(true);

        message.setFriends(friendNames);
        message.setCount(message.getFriends().size());

        return message;
    }

    public Message blockFriendbyUsername(String request, String target){
        Message message = new Message();

        User requestUser = friendRepo.findUserByUsername(request).get(0);
        User targetUser = friendRepo.findUserByUsername(target).get(0);

        Friendships friendships = new Friendships();
        friendships.setUser(requestUser);
        friendships.setFriend(targetUser);
        friendships.setBlocked(true);

        requestUser.getFriends().add(friendships);

        friendRepo.save(requestUser);

        return message;
    }

//    public List<User> getAllUsers(){
////        LOGGER.info("AWA");
////        LOGGER.info(String.valueOf(userRepo.findAll().size()));
//        return userRepo.findAll();
//    }

}
