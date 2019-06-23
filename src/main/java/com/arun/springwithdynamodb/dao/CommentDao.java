package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.xspec.M;
import com.arun.springwithdynamodb.model.Message;

import java.util.List;

public interface CommentDao {

    void createTable();

    Message put(Message message);

    Message get(String itemId, String messageId);

    void delete(String itemId, String messageId);

    List<Message> getAll();

    List<Message> getAllBasedOnItem(String itemId);

    List<Message> getItemMessageGreaterThanSpecifiedRate(String itemId, int rating);

    List<Message> getAllMessageBasedOnUser(String user);

    List<Message> getAllMessageForUserForRatingGreater(String user, Integer rating);
}
