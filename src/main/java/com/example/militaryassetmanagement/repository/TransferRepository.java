package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Base;
import com.example.militaryassetmanagement.entity.Equipment;
import com.example.militaryassetmanagement.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE t.destinationBase = :base
           AND t.equipment = :equipment
           """)
    int getTransferInQuantity(
            @Param("base") Base base,
            @Param("equipment") Equipment equipment);

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE t.sourceBase = :base
           AND t.equipment = :equipment
           """)
    int getTransferOutQuantity(
            @Param("base") Base base,
            @Param("equipment") Equipment equipment);

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE (:base IS NULL OR t.destinationBase = :base)
           AND (:equipmentType IS NULL
                OR LOWER(t.equipment.type) = LOWER(:equipmentType))
           AND FUNCTION('DATE', t.date) < :date
           """)
    int getTransferInQuantityBeforeDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE (:base IS NULL OR t.sourceBase = :base)
           AND (:equipmentType IS NULL
                OR LOWER(t.equipment.type) = LOWER(:equipmentType))
           AND FUNCTION('DATE', t.date) < :date
           """)
    int getTransferOutQuantityBeforeDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE (:base IS NULL OR t.destinationBase = :base)
           AND (:equipmentType IS NULL
                OR LOWER(t.equipment.type) = LOWER(:equipmentType))
           AND FUNCTION('DATE', t.date) = :date
           """)
    int getTransferInQuantityOnDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(t.quantity), 0)
           FROM Transfer t
           WHERE (:base IS NULL OR t.sourceBase = :base)
           AND (:equipmentType IS NULL
                OR LOWER(t.equipment.type) = LOWER(:equipmentType))
           AND FUNCTION('DATE', t.date) = :date
           """)
    int getTransferOutQuantityOnDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);
}


