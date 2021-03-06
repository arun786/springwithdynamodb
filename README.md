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
 
 For details [click here](https://github.com/arun786/springwithdynamodb/blob/master/HowToConnectToDynamoDB.md)

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


## Using Hibernate 

   for details [click here](https://github.com/arun786/springwithdynamodb/blob/master/HowToConnecToDynamoDBUsingORM.md)


        package com.arun.springwithdynamodb.model;
    
        import com.amazonaws.services.dynamodbv2.datamodeling.*;
        import lombok.Getter;
        import lombok.Setter;
        
        @DynamoDBTable(tableName = "User")
        @Getter
        @Setter
        public class User {
            @DynamoDBHashKey
            @DynamoDBAutoGeneratedKey
            private String userId;
            @DynamoDBAttribute
            private String name;
            @DynamoDBAttribute
            private String age;
            @DynamoDBAttribute
            private String address;
        }
    
    
        package com.arun.springwithdynamodb.dao;
        
        import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
        import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
        import com.arun.springwithdynamodb.model.User;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;
        
        import java.util.List;
        
        @Repository
        public class UserDaoImpl implements UserDao {
        
            private DynamoDBMapper dynamoDBMapper;
        
            @Autowired
            public UserDaoImpl(DynamoDBMapper dynamoDBMapper) {
                this.dynamoDBMapper = dynamoDBMapper;
            }
        
            @Override
            public User addUser(User user) {
                dynamoDBMapper.save(user);
                return user;
            }
        
            @Override
            public User getUser(String id) {
                return dynamoDBMapper.load(User.class, id);
            }
        
            @Override
            public void delete(String id) {
                User user = new User();
                user.setUserId(id);
                dynamoDBMapper.delete(user);
            }
        
            @Override
            public List<User> getAllUser() {
                return dynamoDBMapper.scan(User.class, new DynamoDBScanExpression());
            }
        }

## Optimistic Locking

    Steps for Optimistic locking,
    
    Model should have a version attribute
    
        /**
         * This is for optimistic locking
         */
        @DynamoDBVersionAttribute
        private Long version;
        
        
        where we are updating, we need to catch the exception and retrieve the value again
        
        @Override
        public User updateUser(User user) {
        
            OptimisticLocking(user);
            return user;
        }
        
        private void OptimisticLocking(User user) {
            User user1 = getUser(user.getUserId());
            User user2 = getUser(user.getUserId());
        
            updateAge(user1);
            updateAddress(user2);
        }
        
        private void updateAge(User user1) {
            user1.setAge("10");
            dynamoDBMapper.save(user1);
        }
        
        private void updateAddress(User user2) {
            while (true) {
                try {
                    user2.setAddress("This is user 2 address");
                    dynamoDBMapper.save(user2);
                    break;
                } catch (ConditionCheckFailureException c) {
                    user2 = getUser(user2.getUserId());
                }
            }
        }
    
    
    if we want to ignore optimistic locking, we need to 
    
    
        @Override
        public User addUser(User user) {
            /**
             * This disables optimistic locking, basically ignores versioning
             */
            DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig.builder()
                    .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
                    .build();
        
            dynamoDBMapper.save(user, dynamoDBMapperConfig);
            return user;
        }