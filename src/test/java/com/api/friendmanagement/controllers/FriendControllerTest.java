package com.api.friendmanagement.controllers;

import com.api.friendmanagement.models.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String username1 = randomString();
    private static final String username2 = randomString();

    private static String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Test
    @Order(1)
    private void registerNewUsers() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriendsList(new ArrayList<>());
        friends.getFriendsList().add(username1 + "@example.com");
        friends.getFriendsList().add(username2 + "@example.com");

        RegisterMessage registerMessage = new RegisterMessage(true, "Requested to register 2 users. Success : 2 and Failed 0");
        ResponseEntity<RegisterMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/register").toString(),friends, RegisterMessage.class);

        assertEquals(registerMessage.getMessage(), response.getBody().getMessage());

        registerMessage = new RegisterMessage("Requested to register 2 users. Success : 0 and Failed 2");
        response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/register").toString(),friends, RegisterMessage.class);

        assertEquals(registerMessage.getMessage(), response.getBody().getMessage());
    }

    @Test
    @Order(2)
    private void addFriendship() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriendsList(new ArrayList<>());
        friends.getFriendsList().add(username1 + "@example.com");
        friends.getFriendsList().add(username2 + "@example.com");

        GeneralMessage generalMessage = new GeneralMessage(true);
        ResponseEntity<GeneralMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/add").toString(),friends, GeneralMessage.class);
        assertEquals(generalMessage.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(3)
    private  void getFriendsByUserName() throws MalformedURLException {
        Email email = new Email();
        email.setEmailId(username1+"@example.com");
        List<String> friendList = new ArrayList<>(1);
        friendList.add(username2+"@example.com");
        FriendListMessage friendListMessage = new FriendListMessage(true,friendList, friendList.size());

        ResponseEntity<FriendListMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/list").toString(),email, FriendListMessage.class);

        assertEquals(friendListMessage.getFriends().get(0), response.getBody().getFriends().get(0));
    }

    @Test
    @Order(4)
    private void getCommonByUserNames() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriendsList(new ArrayList<>());
        friends.getFriendsList().add(username1 + "@example.com");
        friends.getFriendsList().add(username2 + "@example.com");

        List<String> friendList = new ArrayList<>(1);
        friendList.add(username2+"@example.com");
        FriendListMessage friendListMessage = new FriendListMessage(true,friendList, friendList.size());
        friendListMessage.setSuccess(true);
        ResponseEntity<FriendListMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/common").toString(),friends, FriendListMessage.class);
        assertEquals(friendListMessage.getFriends().get(0), response.getBody().getFriends().get(0));

    }

    @Test
    @Order(5)
    private void addSubscription() throws MalformedURLException {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequester(username1 + "@example.com");
        requestModel.setTarget(username2 + "@example.com");

        GeneralMessage generalMessage = new GeneralMessage(true);
        ResponseEntity<GeneralMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/subscribe").toString(),requestModel, GeneralMessage.class);
        assertEquals(generalMessage.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(6)
    private void addBlock() throws MalformedURLException {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequester(username1 + "@example.com");
        requestModel.setTarget(username2 + "@example.com");

        GeneralMessage generalMessage = new GeneralMessage(true);
        ResponseEntity<GeneralMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/block").toString(),requestModel, GeneralMessage.class);
        assertEquals(generalMessage.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(7)
    private void getSubscribersByUserName() throws MalformedURLException {
        NotifyModel notifyModel = new NotifyModel();
        notifyModel.setSender(username1 + "@example.com");
        notifyModel.setText("Hello World! demo@example.com");

        RecipientMessage recipientMessage = new RecipientMessage(true);
        ResponseEntity<RecipientMessage> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friendsList/notify").toString(),notifyModel, RecipientMessage.class);
        assertEquals(recipientMessage.isSuccess(), response.getBody().isSuccess());
    }
}
