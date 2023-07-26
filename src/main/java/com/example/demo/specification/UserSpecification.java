package com.example.demo.specification;

import com.example.demo.api.entity.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {
    public static Specification<Users> equalUserId(String userId) {
        return new Specification<Users>() {
            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("userId"), userId);
            }
        };
    }

    public static Specification<Users> equalRegNo(String regNo) {
        return new Specification<Users>() {
            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("regNo"), regNo);
            }
        };
    }

    public static Specification<Users> likeUserId(String userId) {
        return new Specification<Users>() {
            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("userId"), "%" + userId + "%");
            }
        };
    }

    public static Specification<Users> likeUserName(String userName) {
        return new Specification<Users>() {
            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("userName"), "%" + userName + "%");
            }
        };
    }
}
