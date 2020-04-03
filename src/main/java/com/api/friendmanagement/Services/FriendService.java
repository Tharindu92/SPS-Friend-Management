package com.api.friendmanagement.Services;

//import com.microservices.signin.Repositories.UserRepo;
//import com.microservices.signin.Models.Message;
//import com.microservices.signin.Models.User;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import com.api.friendmanagement.Models.Friends;
import com.api.friendmanagement.Models.Message;
import com.api.friendmanagement.Models.User;
import com.api.friendmanagement.Repositories.FriendRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Repository
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

//    private static final Logger LOGGER= LogManager.getLogger(UserAuthService.class);

    public Message addFriend(Friends friends){
        User user1 = new User();
        user1.setUsername(friends.getFriends().get(0));
        User user2 = new User();
        user2.setUsername(friends.getFriends().get(1));

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        friendRepo.save(user1);
        friendRepo.save(user2);
        Message message = new Message();
        message.setSuccess(true);
        return message;
    }

//    public List<User> getAllUsers(){
////        LOGGER.info("AWA");
////        LOGGER.info(String.valueOf(userRepo.findAll().size()));
//        return userRepo.findAll();
//    }

}
