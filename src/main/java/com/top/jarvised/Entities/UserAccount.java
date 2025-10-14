package com.top.jarvised.Entities;

import com.top.jarvised.Enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private AccountType accountType;
    private String token;

    @Column(nullable = false)
    private Long schoolId;

    public UserAccount() {}

    public UserAccount(String newEmail, String newPassword, Long schoolId) {
        this.email = newEmail;
        this.password = newPassword;
        this.accountType = AccountType.Master;
        this.token = "";
        this.schoolId = schoolId;
    }

    public UserAccount(Long id, String email, String password, AccountType accountType, Long schoolId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.token = "";
        this.schoolId = schoolId;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public Long getSchoolId() {
        return this.schoolId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

}