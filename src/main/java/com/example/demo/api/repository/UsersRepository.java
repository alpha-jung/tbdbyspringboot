package com.example.demo.api.repository;

import com.example.demo.api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByUserId(String userId);
}
