package com.arun.springwithdynamodb.bootstrap;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class Utils {

    public static void createTable(Class<?> itemClass, DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB) {
        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(itemClass);
        createTableRequest.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        if (!CollectionUtils.isEmpty(createTableRequest.getGlobalSecondaryIndexes())) {
            createTableRequest.getGlobalSecondaryIndexes().forEach(gsi -> {
                gsi.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
                gsi.withProjection(new Projection().withProjectionType("ALL"));
            });
        }

        if (!CollectionUtils.isEmpty(createTableRequest.getLocalSecondaryIndexes())) {
            createTableRequest.getGlobalSecondaryIndexes().forEach(lsi ->
                    lsi.withProjection(new Projection().withProjectionType("ALL")));
        }

        if (!tableExist(amazonDynamoDB, createTableRequest)) {
            amazonDynamoDB.createTable(createTableRequest);
        }
        waitForTableToBeCreated(createTableRequest.getTableName(), amazonDynamoDB);
    }

    private static void waitForTableToBeCreated(String tableName, AmazonDynamoDB amazonDynamoDB) {
        while (true) {
            try {
                Thread.sleep(500);
                TableDescription table = amazonDynamoDB.describeTable(new DescribeTableRequest(tableName)).getTable();
                if (StringUtils.isEmpty(table)) {
                    continue;
                }
                String tableStatus = table.getTableStatus();
                if (tableStatus.equals(TableStatus.ACTIVE.toString())) {
                    return;
                }
            } catch (ResourceNotFoundException e) {
                //ignore
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

    private static boolean tableExist(AmazonDynamoDB amazonDynamoDB, CreateTableRequest createTableRequest) {

        try {
            amazonDynamoDB.describeTable(createTableRequest.getTableName());
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }

    }
}
