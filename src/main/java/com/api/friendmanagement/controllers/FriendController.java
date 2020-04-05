package com.api.friendmanagement.controllers;

import com.api.friendmanagement.constants.MessageConstant;
import com.api.friendmanagement.exceptions.UserBlockedException;
import com.api.friendmanagement.exceptions.UserNotExistsException;
import com.api.friendmanagement.models.*;
import com.api.friendmanagement.services.FriendService;
import com.api.friendmanagement.services.FriendshipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/friends")
@Api(value = "SPS Friend Management", description = "Rest APIs to manage notification process between friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendshipService friendshipService;

    @ApiOperation(value = "API to register a list of email addresses", response = RegisterMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered new users"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/register")
    public ResponseEntity<RegisterMessage> addFriends(@RequestBody Friends friends) {
        try {
            if(friends.getFriendsList() == null || friends.getFriendsList().isEmpty() || !validateInputUsernames(friends.getFriendsList())){
                return new ResponseEntity<>(new RegisterMessage(false, MessageConstant.EMPTY_USER_LIST), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(friendService.addFriends(friends), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new RegisterMessage(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "API to create a friend connection between two email addresses", response = GeneralMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create friendship between 2 existing users"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/add")
    public ResponseEntity<GeneralMessage> addFriendships(@RequestBody Friends friends) {
        try {
            if(friends.getFriendsList() == null || friends.getFriendsList().size() != 2 || !validateInputUsernames(friends.getFriendsList())){
                return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.BAD_REQUEST);
            }
            User friend = friendService.getUserByUserName(friends.getFriendsList().get(0));
            User friendWith = friendService.getUserByUserName(friends.getFriendsList().get(1));

            return new ResponseEntity<>(friendshipService.addFriendship(friend, friendWith), HttpStatus.OK);
        }catch (UserNotExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (UserBlockedException e){
            e.printStackTrace();
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "API to retrieve the friends list for an email address.", response = FriendListMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieve list of friends for a given user"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/list")
    public ResponseEntity<FriendListMessage> getFriendsByEmail(@RequestBody Email email){
        try{
            if(email.getEmailId() == null || !validateInputUsername(email.getEmailId())){
                return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.BAD_REQUEST);
            }
            friendService.getUserByUserName(email.getEmailId());
            return new ResponseEntity<>(friendService.getFriendsByEmail(email), HttpStatus.OK);

        }catch (UserNotExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "API to retrieve the common friends list between two email addresses", response = FriendListMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieve list of common friends for a given 2 users"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/common")
    public ResponseEntity<FriendListMessage> getCommonByUserNames(@RequestBody Friends friends){
        try {
            if(friends.getFriendsList() == null || friends.getFriendsList().size() != 2 || !validateInputUsernames(friends.getFriendsList())){
                return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.BAD_REQUEST);
            }
            friendService.getUserByUserName(friends.getFriendsList().get(0));
            friendService.getUserByUserName(friends.getFriendsList().get(1));
            return new ResponseEntity<>(friendService.getCommonFriends(friends), HttpStatus.OK);
        }catch (UserNotExistsException  e){
            e.printStackTrace();
            return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new FriendListMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "API to subscribe to updates from an email address", response = GeneralMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create a subscription between requester and target"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/subscribe")
    public ResponseEntity<GeneralMessage> addSubscription(@RequestBody RequestModel requestModel){
        try {
            if(requestModel.getTarget() == null || requestModel.getRequester() == null || !validateInputUsername(requestModel.getRequester()) || !validateInputUsername(requestModel.getTarget())){
                System.out.println(requestModel);
                return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.BAD_REQUEST);
            }
            User requester = friendService.getUserByUserName(requestModel.getRequester());
            User target = friendService.getUserByUserName(requestModel.getTarget());

            return new ResponseEntity<>(friendshipService.addSubscription(requester, target), HttpStatus.OK);
        }catch (UserNotExistsException | UserBlockedException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "API to block updates from an email address", response = GeneralMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create a block between requester and target"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/block")
    public ResponseEntity<GeneralMessage> addBlock(@RequestBody RequestModel requestModel) {
        try {
            if (requestModel.getTarget() == null || requestModel.getRequester() == null || !validateInputUsername(requestModel.getRequester()) || !validateInputUsername(requestModel.getTarget())) {
                return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.BAD_REQUEST);
            }
            User requester = friendService.getUserByUserName(requestModel.getRequester());
            User target = friendService.getUserByUserName(requestModel.getTarget());

            return new ResponseEntity<>(friendshipService.addBlock(requester, target), HttpStatus.OK);
        }catch (UserNotExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new GeneralMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiOperation(value = "API to retrieve all email addresses that can receive updates from an email address", response = RecipientMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrive the list of users who get updates"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/notify")
    public ResponseEntity<RecipientMessage> getSubscribersByUserName(@RequestBody NotifyModel notifyModel){
        try {
            if(notifyModel.getSender() == null || notifyModel.getText() == null  || !validateInputUsername(notifyModel.getSender())) {
                return new ResponseEntity<>(new RecipientMessage(false), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(friendService.getSubscribersByUserName(notifyModel), HttpStatus.OK);
        } catch (UserNotExistsException  e) {
            e.printStackTrace();
            return new ResponseEntity<>(new RecipientMessage(false), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new RecipientMessage(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    Validate Email addresses
     */
    public boolean validateInputUsernames(List<String> emails){
        for(String email : emails){
            if(!validateInputUsername(email)){
                return false;
            }
        }
        return true;
    }

    public boolean validateInputUsername(String email){
        if(!Pattern.matches(MessageConstant.EMAIL_REGEX, email)){
            return false;
        }
        return true;
    }
}
