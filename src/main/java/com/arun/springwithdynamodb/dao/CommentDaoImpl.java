package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.arun.springwithdynamodb.bootstrap.Utils;
import com.arun.springwithdynamodb.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CommentDaoImpl implements CommentDao {

    private DynamoDBMapper dynamoDBMapper;
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public CommentDaoImpl(DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @Override
    public void createTable() {

        Utils.createTable(Message.class, dynamoDBMapper, amazonDynamoDB);

    }

    @Override
    public Message put(Message message) {
        dynamoDBMapper.save(message);
        return message;
    }

    @Override
    public Message get(String itemId, String messageId) {

        log.info("ItemId={}, MessageId={}", itemId, messageId);
        Message message = new Message();
        message.setItemId(itemId);
        message.setMessageId(messageId);
        return dynamoDBMapper.load(message);
    }
}
