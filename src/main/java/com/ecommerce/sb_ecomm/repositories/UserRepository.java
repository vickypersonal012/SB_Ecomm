package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByUserName(String username);
}
