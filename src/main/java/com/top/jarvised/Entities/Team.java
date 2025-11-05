package com.top.jarvised.Entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Long schoolId;

    // Many-to-Many relationship with UserAccount (Teachers and Administrators)
    @ManyToMany
    @JoinTable(
        name = "team_members",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_account_id")
    )
    private Set<UserAccount> members = new HashSet<>();

    // Many-to-Many relationship with Code
    @ManyToMany
    @JoinTable(
        name = "team_codes",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "code_id")
    )
    private Set<Code> codes = new HashSet<>();

    public Team() {}

    public Team(String name, String description, Long schoolId) {
        this.name = name;
        this.description = description;
        this.schoolId = schoolId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Set<UserAccount> getMembers() {
        return members;
    }

    public void setMembers(Set<UserAccount> members) {
        this.members = members;
    }

    public Set<Code> getCodes() {
        return codes;
    }

    public void setCodes(Set<Code> codes) {
        this.codes = codes;
    }

    // Helper methods for managing members
    public void addMember(UserAccount user) {
        this.members.add(user);
    }

    public void removeMember(UserAccount user) {
        this.members.remove(user);
    }

    // Helper methods for managing codes
    public void addCode(Code code) {
        this.codes.add(code);
    }

    public void removeCode(Code code) {
        this.codes.remove(code);
    }
}
