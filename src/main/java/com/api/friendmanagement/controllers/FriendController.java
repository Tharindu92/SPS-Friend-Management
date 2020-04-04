package com.api.friendmanagement.controllers;

import com.api.friendmanagement.exceptions.UserBlockedException;
import com.api.friendmanagement.exceptions.UserNotExistsException;
import com.api.friendmanagement.models.*;
import com.api.friendmanagement.services.FriendService;
import com.api.friendmanagement.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/friendsList")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RestController
@RequestMapping(value = "/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping(value = "/register")
    @Path("/register")
    @POST
    public Message addFriends(@RequestBody Friends friends) {
        return friendService.addFriends(friends);
    }

    @PostMapping(value = "/add")
    @Path("/add")
    @POST
    public Message addFriendships(@RequestBody Friends friends) throws UserBlockedException, UserNotExistsException {
        User user1 = friendService.getUserByUserName(friends.getFriendsList().get(0));
        User user2 = friendService.getUserByUserName(friends.getFriendsList().get(1));

        return friendshipService.addFriendship(user1,user2);
    }

    @PostMapping(value = "/list")
    @Path("/list")
    @POST
    public Message getFriendsByUserName(@RequestBody Email email){
        return friendService.getFriendsByUserName(email);
    }

    @PostMapping(value = "/common")
    @Path("/common")
    @POST
    public Message getCommonByUserNames(@RequestBody Friends friends){
        return friendService.getCommonByUserNames(friends);
    }

    @PostMapping(value = "/subscribe")
    @Path("/subscribe")
    @POST
    public Message addSubscription(@RequestBody RequestModel requestModel) throws UserNotExistsException, UserBlockedException {
        User requestor = friendService.getUserByUserName(requestModel.getRequestor());
        User target = friendService.getUserByUserName(requestModel.getTarget());

        if (requestor == null || target == null )
            throw new UserNotExistsException("One of requestor or target is not registered");


        return friendshipService.addSubscription(requestor,target);
    }

    @PostMapping(value = "/block")
    @Path("/block")
    @POST
    public Message addBlock(@RequestBody RequestModel requestModel) throws UserNotExistsException {
        User requestor = friendService.getUserByUserName(requestModel.getRequestor());
        User target = friendService.getUserByUserName(requestModel.getTarget());


        return friendshipService.addBlock(requestor,target);
    }

    @PostMapping(value = "/notify")
    @Path("/notify")
    @POST
    public Message getSubscribersByUserName(@RequestBody NotifyModel notifyModel) throws UserNotExistsException {

        return friendService.getSubscribersByUserName(notifyModel);
    }
}
