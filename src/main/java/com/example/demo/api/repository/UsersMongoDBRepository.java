package com.example.demo.api.repository;

import com.example.demo.api.entity.UserMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersMongoDBRepository extends MongoRepository<UserMongoDB, String> {
    Optional<UserMongoDB> findByEmail(String email);
    Optional<UserMongoDB> findByEmailAndPassword(String email, String password);
}
