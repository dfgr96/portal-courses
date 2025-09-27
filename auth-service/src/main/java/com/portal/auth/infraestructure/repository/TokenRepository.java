package com.portal.auth.infraestructure.repository;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import java.util.Map;

@Repository
public class TokenRepository {

    private final DynamoDbClient dynamoDbClient;

    public TokenRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveToken(String token, String userId, long expiresAt) {
        Map<String, AttributeValue> item = Map.of(
                "token", AttributeValue.builder().s(token).build(),
                "userId", AttributeValue.builder().s(userId).build(),
                "expiresAt", AttributeValue.builder().n(String.valueOf(expiresAt)).build()
        );

        String tableName = "auth_tokens";
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
