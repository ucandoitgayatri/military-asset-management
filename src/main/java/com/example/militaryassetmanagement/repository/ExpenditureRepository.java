package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Base;
import com.example.militaryassetmanagement.entity.Equipment;
import com.example.militaryassetmanagement.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Integer> {

    @Query("""
           SELECT COALESCE(SUM(e.quantity), 0)
           FROM Expenditure e
           WHERE e.base = :base
           AND e.equipment = :equipment
           """)
    int getExpendedQuantity(
            @Param("base") Base base,
            @Param("equipment") Equipment equipment);

    @Query("""
           SELECT COALESCE(SUM(e.quantity), 0)
           FROM Expenditure e
           WHERE (:base IS NULL OR e.base = :base)
           AND (:equipmentType IS NULL
                OR LOWER(e.equipment.type) = LOWER(:equipmentType))
           AND e.date < :date
           """)
    int getExpenditureQuantityBeforeDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(e.quantity), 0)
           FROM Expenditure e
           WHERE (:base IS NULL OR e.base = :base)
           AND (:equipmentType IS NULL
                OR LOWER(e.equipment.type) = LOWER(:equipmentType))
           AND e.date = :date
           """)
    int getExpenditureQuantityOnDate(
            @Param("base") Base base,
            @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);
}
