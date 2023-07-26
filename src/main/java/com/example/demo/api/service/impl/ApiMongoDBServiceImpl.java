package com.example.demo.api.service.impl;

import com.example.demo.api.entity.RefreshToken;
import com.example.demo.api.entity.UserMongoDB;
import com.example.demo.api.exception.ApiErrorCode;
import com.example.demo.api.exception.RestApiException;
import com.example.demo.api.repository.RefreshTokenRepository;
import com.example.demo.api.repository.UsersMongoDBRepository;
import com.example.demo.api.service.ApiMongoDBService;
import com.example.demo.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiMongoDBServiceImpl implements ApiMongoDBService {
    private final UsersMongoDBRepository usersMongoDBRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    private List<String> roles;

    @Override
    public UserMongoDB getUserInfo(String email) {
        UserMongoDB users = null;
        Optional<UserMongoDB> optional = usersMongoDBRepository.findByEmail(email);

        if(optional.isPresent()) {
            users = optional.get();
        }

        return users;
    }

    @Override
    public String regist(Map<String, Object> requestData) {
        String result = "fail";
        String name = requestData.get("name") == null ? "" : requestData.get("name").toString();
        String email = requestData.get("email") == null ? "" : requestData.get("email").toString();
        String password = requestData.get("password") == null ? "" : requestData.get("password").toString();

        if(name.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_USER_NAME);
        }

        if(email.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_USER_EMAIL);
        }

        if(password.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_USER_PASSWORD);
        }

        Optional<UserMongoDB> emailChk = usersMongoDBRepository.findByEmail(email);

        if(emailChk.isPresent()) {
            System.out.println("duplicated");
            throw new RestApiException(ApiErrorCode.DUPLICATED_USER_EMAIL);
        } else {
            UserMongoDB user = new UserMongoDB(name, email, password);
            usersMongoDBRepository.save(user);
            result = "success";
        }

        return result;
    }

    @Override
    public Map<String, Object> loginChk(Map<String, Object> requestData) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = "fail";
        String email = requestData.get("email") == null ? "" : requestData.get("email").toString();
        String password = requestData.get("password") == null ? "" : requestData.get("password").toString();

        if(email.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_USER_EMAIL);
        }

        if(password.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_USER_PASSWORD);
        }

        Optional<UserMongoDB> emailChk = usersMongoDBRepository.findByEmail(email);

        if(!emailChk.isPresent()) {
            throw new RestApiException(ApiErrorCode.NO_MATCHING_USER_INFO);
        } else {
            Optional<UserMongoDB> passwordChk = usersMongoDBRepository.findByEmailAndPassword(email, password);

            if(!passwordChk.isPresent()) {
                throw new RestApiException(ApiErrorCode.WRONG_USER_PASSWORD);
            } else {
                result = "success";

                Map<String, Object> atMap = jwtProvider.createAccessToken(email, roles);
                ResponseCookie responseCookie = jwtProvider.createRefreshToken(email, roles);

                String accessToken = atMap.get("accessToken").toString();

                resultMap.put("status", "success");
                resultMap.put("accessToken", accessToken);
                resultMap.put("expireAt", atMap.get("expireAt").toString());
                resultMap.put("responseCookie", responseCookie);
            }
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        Map<String, Object> resultMap = new HashMap<>();

        if(refreshToken == null || refreshToken.equals("")) {
            throw new RestApiException(ApiErrorCode.EMPTY_REFRESH_TOKEN);
        }

        refreshToken = refreshToken.split("=")[1];
        Optional<RefreshToken> rt = refreshTokenRepository.findById(refreshToken);

        if(!jwtProvider.validationToken(refreshToken, "refresh") || !rt.isPresent()) {
            throw new RestApiException(ApiErrorCode.EXPIRED_REFRESH_TOKEN);
        } else {
            roles = new ArrayList<>();
            roles.add("ROLE_USER");

            //String userId = jwtProvider.getUserPK(refreshToken, "refresh");
            String email = rt.get().getEmail();
            //String accessToken = jwtProvider.createAccessToken(userId, roles);

            Map<String, Object> atMap = jwtProvider.createAccessToken(email, roles);
            String accessToken = atMap.get("accessToken").toString();

            resultMap.put("status", "success");
            resultMap.put("accessToken", accessToken);
            resultMap.put("expireAt", atMap.get("expireAt").toString());
        }

        return resultMap;
    }
}
