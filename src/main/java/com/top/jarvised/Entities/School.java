package com.top.jarvised.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dbUrl;
    private String schoolName;

    public School() {}

    public Long getId() {
        return id;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public String getDbUrl() {
        return dbUrl;
    }
    
}

