package com.arun.springwithdynamodb.dao;

import com.arun.springwithdynamodb.model.Message;

import java.util.List;

public interface CommentDao {

    void createTable();

    Message put(Message message);

    Message get(String itemId, String messageId);

    void delete(String itemId, String messageId);

    List<Message> getAll();
}
