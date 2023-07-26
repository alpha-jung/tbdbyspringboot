package com.example.demo.api.controller;

import com.example.demo.api.entity.UserMongoDB;
import com.example.demo.api.service.ApiMongoDBService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mongo")
public class ApiMongoDBController {
    private final ApiMongoDBService apiMongoDBService;

    @PostMapping("/users/regist")
    public String regist(@RequestBody Map<String, Object> requestData) throws Exception {
        return apiMongoDBService.regist(requestData);
    }

    @PostMapping("/users/loginChk")
    public String loginChk(@RequestBody Map<String, Object> requestData, HttpServletResponse response) throws Exception {


        Map<String, Object> resultMap = apiMongoDBService.loginChk(requestData);

        response.setHeader("Authorization", resultMap.get("accessToken").toString());
        response.setHeader("ExpireAt", resultMap.get("expireAt").toString());
        response.setHeader("Set-Cookie", resultMap.get("responseCookie").toString());
        resultMap.remove("responseCookie");

        return resultMap.get("status").toString();
    }

    @GetMapping("/users/{email}")
    public UserMongoDB getUserInfo(@PathVariable String email) throws Exception {
        return apiMongoDBService.getUserInfo(email);
    }

    @PostMapping("/users/silentRefresh")
    public String silentRefresh(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cookies = request.getHeader("cookie");
        String refreshToken = "";

        if(cookies != null && !cookies.equals("")) {
            String[] cookie = cookies.split(";");

            for(int i = 0; i < cookie.length; i++) {
                if(cookie[i].contains("refreshToken")) {
                    refreshToken = cookie[i];
                    break;
                }
            }
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap = apiMongoDBService.refreshToken(refreshToken);

        response.setHeader("Authorization", resultMap.get("accessToken").toString());
        response.setHeader("ExpireAt", resultMap.get("expireAt").toString());

        return resultMap.get("status").toString();
    }
}
