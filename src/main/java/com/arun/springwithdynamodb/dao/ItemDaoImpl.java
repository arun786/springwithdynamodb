package com.arun.springwithdynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.arun.springwithdynamodb.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public ItemDaoImpl(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @Override
    public void createItem(Item item) {
        PutItemRequest putItemRequest = new PutItemRequest("Item", buildMapForAdd(item));
        amazonDynamoDB.putItem(putItemRequest);
    }

    private Map<String, AttributeValue> buildMapForAdd(Item item) {
        Map<String, AttributeValue> map = new HashMap<>();
        String id = UUID.randomUUID().toString();
        map.put("id", new AttributeValue().withS(id));
        if (!StringUtils.isEmpty(item.getName())) {
            map.put("name", new AttributeValue().withS(item.getName()));
        }
        if (!StringUtils.isEmpty(item.getDescription())) {
            map.put("description", new AttributeValue().withS(item.getDescription()));
        }
        map.put("rating", new AttributeValue().withN(Integer.toString(item.getTotalRating())));
        map.put("comments", new AttributeValue().withN(Integer.toString(item.getTotalComments())));
        return map;
    }
}
