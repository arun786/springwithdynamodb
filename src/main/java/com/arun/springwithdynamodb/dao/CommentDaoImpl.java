package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.arun.springwithdynamodb.bootstrap.Utils;
import com.arun.springwithdynamodb.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public void delete(String itemId, String messageId) {
        Message message = new Message();
        message.setMessageId(messageId);
        message.setItemId(itemId);
        dynamoDBMapper.delete(message);
    }

    @Override
    public List<Message> getAll() {
        return dynamoDBMapper.scan(Message.class, new DynamoDBScanExpression());
    }

    @Override
    public List<Message> getAllBasedOnItem(String itemId) {
        Message message = new Message();
        message.setItemId(itemId);
        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message);
        return dynamoDBMapper.query(Message.class, queryExpression);
    }

    /**
     * @param itemId - partition key
     * @param rating - LSI
     * @return
     */
    @Override
    public List<Message> getItemMessageGreaterThanSpecifiedRate(String itemId, int rating) {
        Message message = new Message();
        message.setItemId(itemId);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message)
                .withRangeKeyCondition("rating", new Condition()
                        .withComparisonOperator(ComparisonOperator.GE)
                        .withAttributeValueList(new AttributeValue().withN(Integer.toString(rating))));

        return dynamoDBMapper.query(Message.class, queryExpression);
    }

    /**
     * @param user - this is a GSI, GSI can be read only with eventual Consistent read
     * @return
     */
    @Override
    public List<Message> getAllMessageBasedOnUser(String user) {

        Message message = new Message();
        message.setUserId(user);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message)
                .withConsistentRead(false);

        return dynamoDBMapper.query(Message.class, queryExpression);
    }

    /**
     * @param user   - GSI
     * @param rating - sort key and LSI
     * @return
     */
    @Override
    public List<Message> getAllMessageForUserForRatingGreater(String user, Integer rating) {

        Message message = new Message();
        message.setUserId(user);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message)
                .withConsistentRead(false)
                .withRangeKeyCondition("rating", new Condition()
                        .withComparisonOperator(ComparisonOperator.GE).withAttributeValueList(
                                new AttributeValue().withN(Integer.toString(rating))
                        ));

        return dynamoDBMapper.query(Message.class, queryExpression);
    }
}
