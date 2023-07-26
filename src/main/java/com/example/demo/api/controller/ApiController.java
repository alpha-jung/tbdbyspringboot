package com.example.demo.api.controller;

import com.example.demo.api.service.ApiService;
import com.example.demo.api.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/users")
    public List<Users> getAllUserList(@RequestParam(required = false) String userId, @RequestParam(required = false) String userName) {
        return apiService.getAllUserList(userId, userName);
    }

    @GetMapping("/users/{userId}")
    public Users getUserInfo(@PathVariable String userId) {
        return apiService.getUserInfo(userId);
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Users users) {
        return apiService.createUser(users);
    }

    @PutMapping("/users")
    public Map<String, Object> updateUser(@RequestBody Users users) {
        return apiService.updateUser(users);
    }

    @DeleteMapping("/users/{userId}")
    public Map<String, Object> deleteUser(@PathVariable String userId) {
        return apiService.deleteUser(userId);
    }
}
