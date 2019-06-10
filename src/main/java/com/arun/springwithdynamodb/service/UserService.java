package com.arun.springwithdynamodb.service;

import com.arun.springwithdynamodb.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User getUser(String id);

    void delete(String id);

    List<User> getAllUser();
}
