package com.arun.springwithdynamodb.service;

import com.arun.springwithdynamodb.dao.UserDao;
import com.arun.springwithdynamodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User getUser(String id) {
        return userDao.getUser(id);
    }

    @Override
    public void delete(String id) {
        userDao.delete(id);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }
}
