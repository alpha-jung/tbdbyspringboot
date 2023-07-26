package com.example.demo.api.service.impl;

import com.example.demo.api.repository.UsersRepository;
import com.example.demo.api.service.ApiService;
import com.example.demo.api.entity.Users;
import com.example.demo.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
    private final UsersRepository usersRepository;

    @Override
    public List<Users> getAllUserList(String userId, String userName) {
        Specification<Users> spec = Specification.where(UserSpecification.likeUserId(userId));
        spec = spec.or(UserSpecification.likeUserName(userName));

        return usersRepository.findAll(spec);
    }

    @Override
    public Users getUserInfo(String userId) {
        Users users = null;
        Optional<Users> optional = usersRepository.findByUserId(userId);

        if(optional.isPresent()) {
            users = optional.get();
        }

        return users;
    }

    @Override
    public Map<String, Object> createUser(Users users) {
        Map<String, Object> resultMap = new HashMap<>();

        String userId = users.getUserId();
        String userName = users.getUserName();
        String userPwd = users.getUserPwd();

        if(userId == null || userId.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "ID를 입력해주세요.");
            return resultMap;
        }

        if(userName == null || userName.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "이름을 입력해주세요.");
            return resultMap;
        }

        if(userPwd == null || userPwd.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "비밀번호를 입력해주세요.");
            return resultMap;
        }

        if(usersRepository.findByUserId(userId) != null) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "이미 존재하는 ID 입니다.");
        } else {
            usersRepository.save(users);

            resultMap.put("status", "SUCCESS");
            resultMap.put("msg", "회원가입이 완료되었습니다.");
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> updateUser(Users users) {
        Map<String, Object> resultMap = new HashMap<>();

        String userId = users.getUserId();
        String userName = users.getUserName();
        String userPwd = users.getUserPwd();

        if(userId == null || userId.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "ID를 입력해주세요.");
            return resultMap;
        }

        if(userName == null || userName.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "이름을 입력해주세요.");
            return resultMap;
        }

        if(userPwd == null || userPwd.equals("")) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "비밀번호를 입력해주세요.");
            return resultMap;
        }

        if(usersRepository.findByUserId(userId) == null) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "해당 ID가 존재하지 않습니다.");
        } else {
            usersRepository.save(users);

            resultMap.put("status", "SUCCESS");
            resultMap.put("msg", "사용자 정보가 수정되었습니다.");
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> deleteUser(String userId) {
        Map<String, Object> resultMap = new HashMap<>();

        if(usersRepository.findByUserId(userId) == null) {
            resultMap.put("status", "FAIL");
            resultMap.put("msg", "해당 ID가 존재하지 않습니다.");
        } else {
            usersRepository.deleteById(userId);

            resultMap.put("status", "SUCCESS");
            resultMap.put("msg", "해당 사용자 정보가 삭제되었습니다.");
        }

        return resultMap;
    }
}
