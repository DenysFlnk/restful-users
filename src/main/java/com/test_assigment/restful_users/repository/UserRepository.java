package com.test_assigment.restful_users.repository;

import com.test_assigment.restful_users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
