package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.arun.springwithdynamodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    public UserDaoImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public User addUser(User user) {
        dynamoDBMapper.save(user);
        return user;
    }

    @Override
    public User getUser(String id) {
        return dynamoDBMapper.load(User.class, id);
    }

    @Override
    public void delete(String id) {
        User user = new User();
        user.setUserId(id);
        dynamoDBMapper.delete(user);
    }

    @Override
    public List<User> getAllUser() {
        return dynamoDBMapper.scan(User.class, new DynamoDBScanExpression());
    }
}
