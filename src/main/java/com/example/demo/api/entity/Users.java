package com.example.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "USER")
public class Users {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_PWD")
    private String userPwd;

    @Builder
    public Users(String userId, String userName, String userPwd) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
    }
}
