package com.example.demo.api.entity;


import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Document(collection = "user_cred")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMongoDB {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String email;
    private String password;

    @Builder
    public UserMongoDB(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
