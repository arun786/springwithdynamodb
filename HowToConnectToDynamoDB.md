# How to connect to DynamoDB programmatically using Spring Boot Config.


Various classes which is required to connect to DynamoDB table
    
    1. BasicAWSCredentials - this is a class which implements AWSCredentials and 
    
    takes in accesskey and secretkey as a constructor parameter.
     
    2. AWSStaticCredentialsProvider
    
    3. AmazonDynamoDB - this is the class which interacts with DynamoDB tables.
    
    
    @Bean
    public AmazonDynamoDB getDynamoDb() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
        return AmazonDynamoDBClientBuilder.standard().withCredentials(awsStaticCredentialsProvider).withRegion(Regions.US_WEST_1).build();
    }

We use Http Request to connect to DynamoDB for any crud operations.
There are DynamoDB API's 

    1. Low Level - where direct map between DynamoDB and HTTP method is done.
    2. Document Interface - Higher level interface to perform CRUD operations
    3. Object Persistence - ORM tools.  


1. Using low level API
    
    Here we use a Map having String as key and values as AttributeValue. 
    
    This map once constructed will be passed to PutItemRequest class
    
    class AmazonDynamoDB with method putItem will be called passing the PutItemRequest object.
     
    Details of classed used in Lowlevel API
        
    1.  PutItemRequest - we specify the table name and the map which in turn is passed to
    
    DynamoDB class method putItem.
    
    2.  AttributeValue - these are the values in the DynamoDB table which is to be populated 
    
    for each column.
    
    
        
        package com.arun.springwithdynamodb.dao;
        
        import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
        import com.amazonaws.services.dynamodbv2.model.AttributeValue;
        import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
        import com.arun.springwithdynamodb.model.Item;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;
        import org.springframework.util.StringUtils;
        
        import java.util.HashMap;
        import java.util.Map;
        
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
                map.put("id", new AttributeValue().withS(item.getId()));
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


    
    
    