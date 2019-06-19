package com.arun.springwithdynamodb.service;

import com.arun.springwithdynamodb.model.Message;

public interface CommentService {

    void createTable();

    Message put(Message message);

    Message get(String itemId, String messageId);
}
