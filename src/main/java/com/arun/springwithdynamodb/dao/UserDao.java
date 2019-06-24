package com.arun.springwithdynamodb.dao;

import com.arun.springwithdynamodb.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User getUser(String id);

    User updateUser(User user);

    void delete(String id);

    List<User> getAllUser();
}
