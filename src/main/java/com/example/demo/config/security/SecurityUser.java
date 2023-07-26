package com.example.demo.config.security;

import com.example.demo.api.entity.Users;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {
    private Users users;

    public SecurityUser(Users users) {
        super(users.getUserId(), users.getUserPwd(), AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }
}
