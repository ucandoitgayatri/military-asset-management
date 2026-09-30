package com.example.militaryassetmanagement.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private int quantity;

    private LocalDate date;

    public Purchase() {
    }

    public Purchase( Base base, Equipment equipment, int quantity) {

        this.base = base;
        this.equipment = equipment;
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
