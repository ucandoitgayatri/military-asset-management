package com.example.militaryassetmanagement.controller;


import com.example.militaryassetmanagement.MilitaryAssetManagementApplication;
import com.example.militaryassetmanagement.entity.Assignment;
import com.example.militaryassetmanagement.entity.Expenditure;
import com.example.militaryassetmanagement.entity.Purchase;
import com.example.militaryassetmanagement.entity.Transfer;

import com.example.militaryassetmanagement.service.MilitaryAssetService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assetManagement")
public class MilitaryAssetController{

    private final MilitaryAssetService militaryAssetService;

    public MilitaryAssetController(MilitaryAssetService militaryAssetService){
        this.militaryAssetService = militaryAssetService;
    }

    @PostMapping("/addPurchase")
    public ResponseEntity<Purchase> addPurchaseMethod(@RequestBody Purchase purchase){
        Purchase p = militaryAssetService.addPurchase(purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @GetMapping("/getAllPurchases")
    public ResponseEntity<List<Purchase>> getAllPurchasesMethod(){
        return ResponseEntity.ok(militaryAssetService.getAllPurchases());
    }

    @PostMapping("/addTransfer")
    public ResponseEntity<Transfer> addTransferMethod(@RequestBody Transfer transfer){
        Transfer t  = militaryAssetService.addTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @GetMapping("/getAllTransfers")
    public ResponseEntity<List<Transfer>> getAllTransfersMethod(){
        return ResponseEntity.ok(militaryAssetService.getAllTransfers());
    }

    @PostMapping("/addAssignment")
    public ResponseEntity<Assignment> addAssignmentMethod(@RequestBody Assignment assignment){
        Assignment a = militaryAssetService.addAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    @GetMapping("/getAllAssignments")
    public ResponseEntity<List<Assignment>> getAllAssignmentsMethod() {
        return ResponseEntity.ok(militaryAssetService.getAllAssignments());
    }


    @PostMapping("/addExpenditure")
    public ResponseEntity<Expenditure> addExpenditureMethod(@RequestBody Expenditure expenditure){
        Expenditure e = militaryAssetService.addExpenditure(expenditure);
        return ResponseEntity.status(HttpStatus.CREATED).body(e);
    }

    @GetMapping("/getAllExpenditures")
    public ResponseEntity<List<Expenditure>> getAllExpendituresMethod(){
        return ResponseEntity.ok(militaryAssetService.getAllExpenditures());
    }


}
