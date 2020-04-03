package com.api.friendmanagement.Controllers;

import com.api.friendmanagement.Models.Friends;
import com.api.friendmanagement.Models.Message;
import com.api.friendmanagement.Services.FriendService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;
//    private static final Logger LOGGER= LogManager.getLogger(UserAuthController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/friends/add")
    public Message getStaffByUserName(@RequestBody Friends friends){
//        LOGGER.info("Hit una");
        return friendService.addFriend(friends);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/user/signin")
//    public List<User> getAllUsers(){
//        return userAuthService.getAllUsers();
//    }
}
