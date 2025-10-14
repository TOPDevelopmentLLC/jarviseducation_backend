package com.top.jarvised.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.top.jarvised.Entities.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByEmailAndPassword(String email, String password);
    
}
