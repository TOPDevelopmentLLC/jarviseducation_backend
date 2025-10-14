package com.top.jarvised.Entities;

import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parents")
public class Parent {

    @Id
    private Long id;
    private String name;

    public Parent() {}

    public Parent(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
}
