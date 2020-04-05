package com.api.friendmanagement.services;

import com.api.friendmanagement.constants.MessageConstant;
import com.api.friendmanagement.exceptions.UserBlockedException;
import com.api.friendmanagement.exceptions.UserNotExistsException;
import com.api.friendmanagement.models.*;
import com.api.friendmanagement.repositories.FriendRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Repository
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

    @Autowired
    private FriendshipService friendshipService;

    public RegisterMessage addFriends(Friends friends) {
        int registered = 0;
        int failed = 0;
        String registerMessage = "Requested to register %d users. Success : %d and Failed %d";
        List<User> userList = new ArrayList<>(friends.getFriendsList().size());
        for(String username : friends.getFriendsList()){
            List<User> checkuserList = friendRepo.findUserByUsername(username);

            if(checkuserList.isEmpty()) {
                userList.add(new User(username));
                registered++;
            }else{
                failed++;
            }
        }
        if(!userList.isEmpty()){
            friendRepo.saveAll(userList);
        }
        return new RegisterMessage(true, String.format(registerMessage, registered+failed, registered, failed));
    }

    public FriendListMessage getFriendsByEmail(Email email) {
        List<User> friends = friendRepo.findFriendsByUsername(email.getEmailId());
        return getFriendList(friends);
    }

    public FriendListMessage getCommonFriends(Friends friends) {
        List<User> friendsList = friendRepo.findCommonByUsernames(friends.getFriendsList().get(0), friends.getFriendsList().get(1));
        return getFriendList(friendsList);
    }

    public FriendListMessage getFriendList(List<User> friends){
        List<String> friendNames = null;
        if(friends != null && !friends.isEmpty()){
            friendNames = new ArrayList<>(friends.size());
            for(User friend : friends){
                friendNames.add(friend.getUsername());
            }
            return new FriendListMessage(true, friendNames, friendNames.size());
        }else if (friends.isEmpty()){
            return new FriendListMessage(true, friendNames, 0);
        }else{
            return new FriendListMessage(false);
        }
    }

    public RecipientMessage getSubscribersByUserName(NotifyModel notifyModel) throws UserNotExistsException {
        getUserByUserName(notifyModel.getSender());

        List<User> recipients = friendRepo.findSubscribersByUsername(notifyModel.getSender());
        List<String> recipientsName = extractUsersFromText(notifyModel.getText(), notifyModel.getSender());

        if(recipients != null && !recipients.isEmpty()){
            for(User recipient : recipients){
                recipientsName.add(recipient.getUsername());
            }
        }
        return new RecipientMessage(true, recipientsName);
    }

    public User getUserByUserName(String username) throws UserNotExistsException {

        List<User> user = friendRepo.findUserByUsername(username);
        if(user.isEmpty())
            throw new UserNotExistsException(username);

        return friendRepo.findUserByUsername(username).get(0);
    }

    public List<String> extractUsersFromText(String text, String sender){
        List<String> extractUsers = new ArrayList<>();
        String[] words = text.split(" ");
        for(String word: words){
            if(Pattern.matches(MessageConstant.EMAIL_REGEX, word)){
                try{
                    getUserByUserName(word);
                    if(isBlockedUser(sender, word)){
                        throw new UserBlockedException(sender, word);
                    }

                }catch (UserNotExistsException | UserBlockedException e){
                    continue;
                }
                extractUsers.add(word);
            }
        }
        return extractUsers;
    }

    public boolean isBlockedUser(String blocked, String blockedBy){
        if(friendRepo.isUserBlocked(blocked, blockedBy) == 0) {
            return false;
        }else {
            return true;
        }
    }


}
