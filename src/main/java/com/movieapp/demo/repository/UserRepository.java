package com.movieapp.demo.repository;

import com.movieapp.demo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
