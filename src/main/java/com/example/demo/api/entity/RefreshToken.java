package com.example.demo.api.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {
    @Id
    private String refreshToken;

    private String email;

    @Builder
    public RefreshToken(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
    }
}
