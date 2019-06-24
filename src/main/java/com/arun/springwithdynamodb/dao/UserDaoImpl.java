package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
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
        /**
         * This disables optimistic locking, basically ignores versioning
         */
        DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
                .build();

        dynamoDBMapper.save(user, dynamoDBMapperConfig);
        return user;
    }

    @Override
    public User getUser(String id) {
        return dynamoDBMapper.load(User.class, id);
    }

    @Override
    public User updateUser(User user) {

        OptimisticLocking(user);
        return user;
    }

    private void OptimisticLocking(User user) {

        User user1 = getUser(user.getUserId());
        User user2 = getUser(user.getUserId());

        updateAge(user1);
        updateAddress(user2);

    }

    private void updateAge(User user1) {
        user1.setAge("10");
        dynamoDBMapper.save(user1);
    }

    private void updateAddress(User user2) {

        user2.setAddress("This is user 2 address");
        dynamoDBMapper.save(user2);
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
