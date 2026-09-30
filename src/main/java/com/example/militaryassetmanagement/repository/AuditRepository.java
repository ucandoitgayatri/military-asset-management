package com.example.militaryassetmanagement.repository;

import com.example.militaryassetmanagement.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
