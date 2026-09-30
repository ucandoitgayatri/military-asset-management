package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}
