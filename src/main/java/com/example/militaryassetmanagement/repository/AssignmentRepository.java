package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Assignment;
import com.example.militaryassetmanagement.entity.Base;
import com.example.militaryassetmanagement.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query("""
        SELECT COALESCE(SUM(a.quantity), 0) FROM Assignment a WHERE a.base = :base AND a.equipment = :equipment
        """)

    int getAssignedQuantity(@Param("base") Base base, @Param("equipment") Equipment equipment);

    @Query("""
           SELECT COALESCE(SUM(a.quantity), 0)FROM Assignment a WHERE (:base IS NULL OR a.base = :base)
           AND (:equipmentType IS NULL OR LOWER(a.equipment.type) = LOWER(:equipmentType))
           AND a.date < :date""")

    int getAssignedQuantityBeforeDate(@Param("base") Base base, @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);

    @Query("""
           SELECT COALESCE(SUM(a.quantity), 0) FROM Assignment a WHERE (:base IS NULL OR a.base = :base)
           AND (:equipmentType IS NULL OR LOWER(a.equipment.type) = LOWER(:equipmentType))
           AND a.date = :date""")

    int getAssignedQuantityOnDate(@Param("base") Base base, @Param("equipmentType") String equipmentType,
            @Param("date") LocalDate date);
}