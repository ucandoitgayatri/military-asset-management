package com.example.militaryassetmanagement.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDate;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;


    @Column(name = "person_name")
    private String personName;

    private int quantity;

    private LocalDate date;

    public Assignment() {
    }

    public Assignment(Base base, Equipment equipment, String personName, int quantity) {
        this.base = base;
        this.equipment = equipment;
        this.personName = personName;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }


    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
