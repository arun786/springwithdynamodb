# Spring With DynamoDB

## To Create a record to DynamoDB Table.


### Step 1 : Create a config which will create 


    package com.arun.springwithdynamodb.config;
    
    import com.amazonaws.auth.AWSStaticCredentialsProvider;
    import com.amazonaws.auth.BasicAWSCredentials;
    import com.amazonaws.regions.Regions;
    import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
    import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class DbConfig {
    
        private AwsConfig awsConfig;
    
        @Autowired
        public DbConfig(AwsConfig awsConfig) {
            this.awsConfig = awsConfig;
        }
    
        @Bean
        public AmazonDynamoDB getDynamoDb() {
            BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());
            AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
            return AmazonDynamoDBClientBuilder.standard().withCredentials(awsStaticCredentialsProvider).withRegion(Regions.US_EAST_2).build();
        }
    }
 

### DAO Layer

    package com.arun.springwithdynamodb.dao;
    
    import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
    import com.amazonaws.services.dynamodbv2.model.AttributeValue;
    import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
    import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
    import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
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

