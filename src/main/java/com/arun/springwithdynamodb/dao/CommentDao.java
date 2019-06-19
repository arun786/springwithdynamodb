package com.arun.springwithdynamodb.dao;

import com.arun.springwithdynamodb.model.Message;

public interface CommentDao {

    void createTable();

    Message put(Message message);

    Message get(String itemId, String messageId);
}
