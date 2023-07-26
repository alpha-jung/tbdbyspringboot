package com.example.demo.api.service;

import com.example.demo.api.entity.Users;

import java.util.List;
import java.util.Map;

public interface ApiService {
    List<Users> getAllUserList(String userId, String userName);

    Users getUserInfo(String userId);

    Map<String, Object> createUser(Users users);

    Map<String, Object> updateUser(Users users);

    Map<String, Object> deleteUser(String userId);
}
