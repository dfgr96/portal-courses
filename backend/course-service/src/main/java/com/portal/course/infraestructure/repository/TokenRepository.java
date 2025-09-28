package com.portal.course.infraestructure.repository;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import java.util.Map;
import java.util.Optional;

@Repository
public class TokenRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "auth_tokens";

    public TokenRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public boolean existsToken(String token) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("token", AttributeValue.builder().s(token).build()))
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        return item != null && !item.isEmpty();
    }

    public Optional<String> getUserIdIfValid(String token) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("token", AttributeValue.builder().s(token).build()))
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item != null && !item.isEmpty()) {
            long expiresAt = Long.parseLong(item.get("expiresAt").n());
            if (expiresAt > System.currentTimeMillis()) {
                return Optional.of(item.get("userId").s());
            }
        }
        return Optional.empty();
    }
}
