package com.example.militaryassetmanagement.controller;


import com.example.militaryassetmanagement.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }


    @GetMapping
    public ResponseEntity<Map <String, Integer>> getDashboardDetailsMethod(@RequestParam(required = false) Integer baseId,
                                                          @RequestParam(required = false) String equipmentType,
                                                          @RequestParam(required = false) LocalDate date){
        return ResponseEntity.ok(dashboardService.getDashboardDetails(baseId, equipmentType, date));
    }

}
