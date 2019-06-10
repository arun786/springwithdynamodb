package com.arun.springwithdynamodb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.dynamodb")
@Getter
@Setter
public class AwsConfig {
    private String accessKey;
    private String secretKey;
    private String region;
}
