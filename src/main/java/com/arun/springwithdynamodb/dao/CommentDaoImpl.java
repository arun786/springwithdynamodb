package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.arun.springwithdynamodb.bootstrap.Utils;
import com.arun.springwithdynamodb.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
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
}
