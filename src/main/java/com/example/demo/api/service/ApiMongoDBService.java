package com.example.demo.api.service;

import com.example.demo.api.entity.UserMongoDB;

import java.util.Map;

public interface ApiMongoDBService {
    UserMongoDB getUserInfo(String email);

    String regist(Map<String, Object> requestData);

    Map<String, Object> loginChk(Map<String, Object> requestData);

    Map<String, Object> refreshToken(String refreshToken);
}
