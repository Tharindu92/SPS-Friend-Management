package com.api.friendmanagement.services;

import com.api.friendmanagement.exceptions.UserNotExistsException;
import com.api.friendmanagement.models.*;
import com.api.friendmanagement.repositories.FriendRepo;
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

    public Message addFriends(Friends friends) {
        int registered = 0;
        int failed = 0;
        for(String username : friends.getFriendsList()){
            List<User> userList1 = friendRepo.findUserByUsername(username);
            if(userList1.isEmpty()) {
                friendRepo.save(new User(username));
                registered++;
            }else{
                failed++;
            }
        }
        Message message = new Message();
        message.setSuccess(true);
        message.setMessageText("Requested to register "+ (registered+failed) +" users. Success : "+registered + " and Failed "+ failed);
        return message;
    }

    public Message getFriendsByUserName(Email email) {
        Message message = new Message();
        List<String> friendNames = new ArrayList<>();
        List<User> friends = friendRepo.findFriendsByUsername(email.getEmailId());
        for(User friend : friends){
            friendNames.add(friend.getUsername());
        }
        message.setSuccess(true);

        message.setFriends(friendNames);
        message.setCount(message.getFriends().size());

        return message;
    }

    public Message getCommonByUserNames(Friends friends) {
        Message message = new Message();
        List<String> friendNames = new ArrayList<>();
        List<User> friendsList = friendRepo.findCommonByUsernames(friends.getFriendsList().get(0), friends.getFriendsList().get(1));
        for(User friend : friendsList){
            friendNames.add(friend.getUsername());
        }
        message.setSuccess(true);

        message.setFriends(friendNames);
        message.setCount(message.getFriends().size());

        return message;
    }

    public Message getSubscribersByUserName(NotifyModel notifyModel) throws UserNotExistsException {
        Message message = new Message();
        getUserByUserName(notifyModel.getSender());

        List<User> recipients = friendRepo.findSubscribersByUsername(notifyModel.getSender());
        List<String> recipientsName = extractUsersFromText(notifyModel.getText());

        for(User recipient : recipients){
            recipientsName.add(recipient.getUsername());
        }

        message.setSuccess(true);
        message.setRecipients(recipientsName);

        return message;
    }

    public User getUserByUserName(String username) throws UserNotExistsException {

        List<User> user = friendRepo.findUserByUsername(username);
        if(user.isEmpty())
            throw new UserNotExistsException("One of requestor or target is not registered");

        return friendRepo.findUserByUsername(username).get(0);
    }

    public List<String> extractUsersFromText(String text){
        List<String> extractUsers = new ArrayList<>();
        String[] words = text.split(" ");
        for(String word: words){
            if(word.contains("@") && word.endsWith(".com")){
                extractUsers.add(word);
            }
        }
        return extractUsers;
    }


}
