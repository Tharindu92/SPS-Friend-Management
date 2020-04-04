package com.api.friendmanagement.Controllers;

import com.api.friendmanagement.Models.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    public static String randomString(){
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

//    @Test
//    public void getHello() throws Exception {
//
//        ResponseEntity<String> response = restTemplate.getForEntity(new URL("http://localhost:" + port + "/").toString(), String.class);
//
//        assertEquals("Hello Controller", response.getBody());
//
//    }
    @Test
    @Order(1)
    public void registerNewUsers() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriends(new ArrayList<String>());
        friends.getFriends().add(username1 + "@example.com");
        friends.getFriends().add(username2 + "@example.com");

        Message message = new Message();
        message.setMessage("Requested to register 2 users. Success : 2 and Failed 0");
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/register").toString(),friends, Message.class);

        assertEquals(message.getMessage(), response.getBody().getMessage());

        message = new Message();
        message.setMessage("Requested to register 2 users. Success : 0 and Failed 2");
        response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/register").toString(),friends, Message.class);

        assertEquals(message.getMessage(), response.getBody().getMessage());
    }

    @Test
    @Order(2)
    public void addFriendship() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriends(new ArrayList<String>());
        friends.getFriends().add(username1 + "@example.com");
        friends.getFriends().add(username2 + "@example.com");

        Message message = new Message();
        message.setSuccess(true);
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/add").toString(),friends, Message.class);
        assertEquals(message.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(3)
    public  void getFriendsByUserName() throws MalformedURLException {
        Email email = new Email();
        email.setEmail(username1+"@example.com");
        Message message = new Message();
        message.setFriends(new ArrayList<String>());
        message.getFriends().add(username2+"@example.com");
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/list").toString(),email, Message.class);

        assertEquals(message.getFriends().get(0), response.getBody().getFriends().get(0));
    }

    @Test
    @Order(4)
    public void getCommonByUserNames() throws MalformedURLException {
        Friends friends = new Friends();
        friends.setFriends(new ArrayList<String>());
        friends.getFriends().add(username1 + "@example.com");
        friends.getFriends().add(username2 + "@example.com");

        Message message = new Message();
        message.setSuccess(true);
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/common").toString(),friends, Message.class);
        assertEquals(message.isSuccess(), response.getBody().isSuccess());

    }

    @Test
    @Order(5)
    public void addSubscription() throws MalformedURLException {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestor(username1 + "@example.com");
        requestModel.setTarget(username2 + "@example.com");

        Message message = new Message();
        message.setSuccess(true);
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/subscribe").toString(),requestModel, Message.class);
        assertEquals(message.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(6)
    public void addBlock() throws MalformedURLException {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestor(username1 + "@example.com");
        requestModel.setTarget(username2 + "@example.com");

        Message message = new Message();
        message.setSuccess(true);
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/block").toString(),requestModel, Message.class);
        assertEquals(message.isSuccess(), response.getBody().isSuccess());
    }

    @Test
    @Order(7)
    public void getSubscribersByUserName() throws MalformedURLException {
        NotifyModel notifyModel = new NotifyModel();
        notifyModel.setSender(username1 + "@example.com");
        notifyModel.setText("Hello World! demo@example.com");

        Message message = new Message();
        message.setSuccess(true);
        ResponseEntity<Message> response = restTemplate.postForEntity(new URL("http://localhost:"+port+"/friends/notify").toString(),notifyModel, Message.class);
        assertEquals(message.isSuccess(), response.getBody().isSuccess());
    }
}
