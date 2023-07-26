package com.example.demo.config.security;

import com.example.demo.api.entity.Users;
import com.example.demo.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Users> optional = usersRepository.findByUserId(userId);

        if(!optional.isPresent()) {
            throw new UsernameNotFoundException(userId + " 회원 없음");
        } else {
            Users users = optional.get();
            return new SecurityUser(users);
        }
    }
}
