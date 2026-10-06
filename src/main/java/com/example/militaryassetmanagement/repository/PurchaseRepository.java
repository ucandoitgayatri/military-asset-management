package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Base;
import com.example.militaryassetmanagement.entity.Equipment;
import com.example.militaryassetmanagement.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query("""
           SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE p.base = :base
           AND p.equipment = :equipment""")

    int getPurchasedQuantity(@Param("base") Base base, @Param("equipment") Equipment equipment);


    @Query("""
           SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE (:base IS NULL OR p.base = :base)
           AND (:equipmentType IS NULL OR LOWER(p.equipment.type) = LOWER(:equipmentType))
           AND p.date < :date""")

    int getPurchasedQuantityBeforeDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p
           WHERE (:base IS NULL OR p.base = :base) AND (:equipmentType IS NULL
          OR LOWER(p.equipment.type) = LOWER(:equipmentType))AND p.date = :date""")

    int getPurchasedQuantityOnDate(@Param("base") Base base, @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);
}