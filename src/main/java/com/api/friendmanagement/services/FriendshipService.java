package com.api.friendmanagement.services;

import com.api.friendmanagement.exceptions.UserBlockedException;
import com.api.friendmanagement.models.Friendships;
import com.api.friendmanagement.models.GeneralMessage;
import com.api.friendmanagement.models.User;
import com.api.friendmanagement.repositories.FriendshipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    FriendshipRepo friendshipRepo;

    public GeneralMessage addFriendship(User friend, User friendwith) throws UserBlockedException {

        Friendships friendships = setValues(friend.getIduser(), friendwith.getIduser());
        Friendships friendshipsWith = setValues(friendwith.getIduser(), friend.getIduser());

        if(friendships.isBlocked()){
            throw new UserBlockedException(friend.getUsername(), friendwith.getUsername());
        }else if(friendshipsWith.isBlocked()){
            throw new UserBlockedException(friendwith.getUsername(), friend.getUsername());
        }

        List<Friendships> friendshipsList = new ArrayList<>(2);
        friendshipsList.add(friendships);
        friendshipsList.add(friendshipsWith);
        friendshipRepo.saveAll(friendshipsList);

        return new GeneralMessage(true);
    }

    public GeneralMessage addSubscription(User requester, User target) throws UserBlockedException {

        Friendships friendships = setValues(requester.getIduser(), target.getIduser());

        if(friendships.isBlocked())
            throw new UserBlockedException(target.getUsername(), requester.getUsername());

        friendshipRepo.save(friendships);
        return new GeneralMessage(true);
    }

    public GeneralMessage addBlock(User requester, User target){

        Friendships friendships = setValues(requester.getIduser(), target.getIduser());
        friendships.setBlocked(true);
        friendshipRepo.save(friendships);

        return new GeneralMessage(true);
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
