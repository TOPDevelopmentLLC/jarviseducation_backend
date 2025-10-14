package com.top.jarvised.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    // private Optional<Long> userAccountID;

    public Teacher() {}

    public Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}