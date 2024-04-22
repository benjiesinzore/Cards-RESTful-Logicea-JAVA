package com.logicea.cards.repos;

import com.logicea.cards.models.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserLogin, Integer> {
    UserLogin findByEmail(String email);
}
