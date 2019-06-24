package com.arun.springwithdynamodb.controller;

import com.arun.springwithdynamodb.model.User;
import com.arun.springwithdynamodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users/v1/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User user1 = userService.addUser(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @GetMapping("/users/v1/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user1 = userService.getUser(id);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/v1/user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/v1/user")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser = userService.getAllUser();
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @PutMapping("/users/v1/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User user1 = userService.updateUser(user);
        return new ResponseEntity<>(user1, HttpStatus.ACCEPTED);

    }
}
