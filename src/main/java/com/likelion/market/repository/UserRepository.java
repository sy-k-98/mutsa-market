package com.likelion.market.repository;

import com.likelion.market.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByPassword(String password);
}
