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
        return AmazonDynamoDBClientBuilder.standard().withCredentials(awsStaticCredentialsProvider).withRegion(Regions.US_WEST_1).build();
    }
}
