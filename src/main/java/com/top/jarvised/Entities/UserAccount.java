package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.top.jarvised.Enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private boolean requiresPasswordReset = false;

    private String fullName;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams = new HashSet<>();

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

    public UserAccount(String email, String password, AccountType accountType, Long schoolId, String fullName) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.token = "";
        this.schoolId = schoolId;
        this.fullName = fullName;
        this.requiresPasswordReset = true;
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

    public String getToken() {
        return this.token;
    }

    public boolean getRequiresPasswordReset() {
        return this.requiresPasswordReset;
    }

    public void setRequiresPasswordReset(boolean requiresPasswordReset) {
        this.requiresPasswordReset = requiresPasswordReset;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @JsonIgnore
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

}