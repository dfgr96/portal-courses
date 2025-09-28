package com.portal.course.infraestructure.repository;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TokenRepository {

    private final DynamoDbClient dynamoDbClient;

    public TokenRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public boolean existsToken(String token) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("token", AttributeValue.builder().s(token).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName("auth_tokens")
                .key(key)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        return response.hasItem();
    }
}
