package com.example.militaryassetmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "source_base_id")
    private Base sourceBase;

    @ManyToOne
    @JoinColumn(name = "destination_base_id")
    private Base destinationBase;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private int quantity;

    private LocalDateTime date;

    public Transfer() {
    }

    public Transfer(Base sourceBase, Base destinationBase,
                    Equipment equipment, int quantity) {

        this.sourceBase = sourceBase;
        this.destinationBase = destinationBase;
        this.equipment = equipment;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Base getSourceBase() {
        return sourceBase;
    }

    public void setSourceBase(Base sourceBase) {
        this.sourceBase = sourceBase;
    }

    public Base getDestinationBase() {
        return destinationBase;
    }

    public void setDestinationBase(Base destinationBase) {
        this.destinationBase = destinationBase;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
