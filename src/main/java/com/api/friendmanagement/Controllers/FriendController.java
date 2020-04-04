package com.api.friendmanagement.Controllers;

import com.api.friendmanagement.Exceptions.UserBlockedException;
import com.api.friendmanagement.Exceptions.UserNotExistsException;
import com.api.friendmanagement.Models.*;
import com.api.friendmanagement.Services.FriendService;
import com.api.friendmanagement.Services.FriendshipService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/friends")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RestController
@RequestMapping(value = "/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendshipService friendshipService;
//    private static final Logger LOGGER= LogManager.getLogger(UserAuthController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @Path("/register")
    @POST
    public Message addFriends(@RequestBody Friends friends) {
        return friendService.addFriends(friends);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @Path("/add")
    @POST
    public Message addFriendships(@RequestBody Friends friends) throws UserBlockedException, UserNotExistsException {
        User user1 = friendService.getUserByUserName(friends.getFriends().get(0));
        User user2 = friendService.getUserByUserName(friends.getFriends().get(1));

        return friendshipService.addFriendship(user1,user2);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/list")
    @Path("/list")
    @POST
    public Message getFriendsByUserName(@RequestBody Email email){
        return friendService.getFriendsByUserName(email);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/common")
    @Path("/common")
    @POST
    public Message getCommonByUserNames(@RequestBody Friends friends){
        return friendService.getCommonByUserNames(friends);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/subscribe")
    @Path("/subscribe")
    @POST
    public Message addSubscription(@RequestBody RequestModel requestModel) throws UserNotExistsException, UserBlockedException {
        User requestor = friendService.getUserByUserName(requestModel.getRequestor());
        User target = friendService.getUserByUserName(requestModel.getTarget());

        if (requestor == null || target == null )
            throw new UserNotExistsException("One of requestor or target is not registered");


        return friendshipService.addSubscription(requestor,target);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/block")
    @Path("/block")
    @POST
    public Message addBlock(@RequestBody RequestModel requestModel) throws UserNotExistsException {
        User requestor = friendService.getUserByUserName(requestModel.getRequestor());
        User target = friendService.getUserByUserName(requestModel.getTarget());


        return friendshipService.addBlock(requestor,target);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    @Path("/notify")
    @POST
    public Message getSubscribersByUserName(@RequestBody NotifyModel notifyModel) throws UserNotExistsException {

        return friendService.getSubscribersByUserName(notifyModel);
    }
}
