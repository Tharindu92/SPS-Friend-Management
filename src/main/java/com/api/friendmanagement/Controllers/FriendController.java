package com.api.friendmanagement.Controllers;

import com.api.friendmanagement.Models.Email;
import com.api.friendmanagement.Models.Friends;
import com.api.friendmanagement.Models.Message;
import com.api.friendmanagement.Models.User;
import com.api.friendmanagement.Services.FriendService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;
//    private static final Logger LOGGER= LogManager.getLogger(UserAuthController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/friends/add")
    public Message addFriends(@RequestBody Friends friends){
        return friendService.addFriends(friends);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/friends/list")
    public Message getFriendsByUserName(@RequestBody Email email){
        return friendService.getFriendsByUserName(email);
    }
}
