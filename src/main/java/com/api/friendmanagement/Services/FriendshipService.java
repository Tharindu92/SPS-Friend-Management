package com.api.friendmanagement.Services;

import com.api.friendmanagement.Exceptions.UserBlockedException;
import com.api.friendmanagement.Models.Friends;
import com.api.friendmanagement.Models.Friendships;
import com.api.friendmanagement.Models.Message;
import com.api.friendmanagement.Models.User;
import com.api.friendmanagement.Repositories.FriendshipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    FriendshipRepo friendshipRepo;

    public Message addFriendship(User user1, User user2) throws UserBlockedException {

        Friendships friendships1 = setValues(user1.getIduser(), user2.getIduser());
        Friendships friendships2 = setValues(user2.getIduser(), user1.getIduser());

        if(friendships1.isBlocked() || friendships2.isBlocked())
            throw new UserBlockedException("Trying to be friend with blocked user");

        friendshipRepo.save(friendships2);
        friendshipRepo.save(friendships1);

        Message message = new Message();
        message.setSuccess(true);
        return message;
    }

    public Message addSubscription(User requestor, User target) throws UserBlockedException {

        Friendships friendships = setValues(requestor.getIduser(), target.getIduser());

        if(friendships.isBlocked())
            throw new UserBlockedException("Trying to subscribe with blocked user");

        friendshipRepo.save(friendships);

        Message message = new Message();
        message.setSuccess(true);
        return message;
    }

    public Message addBlock(User requestor, User target){

        Friendships friendships = setValues(requestor.getIduser(), target.getIduser());
        friendships.setBlocked(true);
        friendshipRepo.save(friendships);

        Message message = new Message();
        message.setSuccess(true);
        return message;
    }

    private Friendships setValues(Integer id1, Integer id2){
        Friendships friendships;
        List<Friendships> friendshipList = friendshipRepo.findFriendshipByiduser(id1, id2);
        if(friendshipList.isEmpty()){
            friendships = new Friendships();
            friendships.setIduser(id1);
            friendships.setIdfriend(id2);
        }else{
            friendships = friendshipList.get(0);
        }
        return friendships;
    }
}
