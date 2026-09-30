package com.example.militaryassetmanagement.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "bases")
public class Base {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    private String name;


    public Base() {
    }

    public Base(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
