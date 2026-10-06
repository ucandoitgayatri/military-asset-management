package com.example.militaryassetmanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.militaryassetmanagement.entity.*;
import com.example.militaryassetmanagement.repository.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService{

    private final AssignmentRepository assignmentRepository;
       private final BaseRepository baseRepository;
       private final ExpenditureRepository expenditureRepository;
       private final PurchaseRepository purchaseRepository;
       private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    public DashboardService(AssignmentRepository assignmentRepository, BaseRepository baseRepository,
            ExpenditureRepository expenditureRepository,PurchaseRepository purchaseRepository,
                            TransferRepository transferRepository,UserRepository userRepository) {

        this.assignmentRepository = assignmentRepository;
        this.baseRepository = baseRepository;
        this.expenditureRepository = expenditureRepository;
        this.purchaseRepository = purchaseRepository;
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;

    }


     public Map<String, Integer> getDashboardDetails(Integer baseId,String equipmentType,LocalDate date) {
                        // parameters selected according to filter requirements
                        // BaseId instead of base cause its more useful
                        // Map<String, Integer> because want to show method name and quantity

         User user = getCurrentlyLoggedInUser();

         if (date == null) { date = LocalDate.now(); }//if no date is provided use current date
         Base base = null;

         if (isBaseCommander(user)) {
             if (user.getBase() == null) {
                 throw new RuntimeException("Base Commander has no assigned base");
             }
             base = user.getBase();

         } else if (baseId != null) {

             base = baseRepository.findById(baseId).orElseThrow(() -> new RuntimeException("Base not found"));// baseId != null means if you haven't provided any base then baseId becomes null and this method will not work and exception is not thrown

         }

         int openingBalance = openingBalanceCalculate(base, equipmentType, date);
         int closingBalance = closingBalanceCalculate(base, equipmentType, date);
         int netMovement = netMovementCalculate(base, equipmentType, date);
         int expenditure = expenditureCalculate(base, equipmentType, date);
         int assignment = assignmentCalculate(base, equipmentType, date);

         Map<String, Integer> m =  new HashMap<>();
         m.put("Opening Balance", openingBalance);
         m.put("Closing Balance", closingBalance);
         m.put("NetMovement", netMovement);
         m.put("Expenditure", expenditure);
         m.put("Assignment", assignment);

         return m;

     }


    private int openingBalanceCalculate(Base base, String equipmentType, LocalDate date){
         int purchases = purchaseRepository.getPurchasedQuantityBeforeDate(base, equipmentType,date);
         int transfersIn = transferRepository.getTransferInQuantityBeforeDate(base,equipmentType,date);
         int transferOut = transferRepository.getTransferOutQuantityBeforeDate(base,equipmentType,date);
         int expenditure = expenditureRepository.getExpenditureQuantityBeforeDate(base,equipmentType,date);
         int assignment = assignmentRepository.getAssignedQuantityBeforeDate(base,equipmentType,date);

         int openingBalance = purchases + transfersIn - transferOut - expenditure - assignment;
         return openingBalance;
    }

    private int closingBalanceCalculate(Base base, String equipmentType, LocalDate date){
        int purchases = purchaseRepository.getPurchasedQuantityOnDate(base,equipmentType,date);
        int transferIn = transferRepository.getTransferInQuantityOnDate(base,equipmentType,date);
        int transferOut = transferRepository.getTransferOutQuantityOnDate(base,equipmentType,date);
        int expenditure = expenditureRepository.getExpenditureQuantityOnDate(base,equipmentType,date);
        int assignment = assignmentRepository.getAssignedQuantityOnDate(base,equipmentType,date);

        int openingBalance = openingBalanceCalculate(base,equipmentType,date);
        int closingBalance = openingBalance  + purchases + transferIn - transferOut - expenditure - assignment;

        return  closingBalance;
    }

    private int netMovementCalculate(Base base, String equipmentType, LocalDate date){
        int purchases = purchaseRepository.getPurchasedQuantityOnDate(base,equipmentType,date);
        int transferIn = transferRepository.getTransferInQuantityOnDate(base,equipmentType,date);
        int transferOut = transferRepository.getTransferOutQuantityOnDate(base,equipmentType,date);

        int netMovement = purchases + transferIn - transferOut;
        return  netMovement;
    }

    private int expenditureCalculate(Base base,String equipmentType, LocalDate date){
        int expenditure = expenditureRepository.getExpenditureQuantityOnDate(base,equipmentType,date);
        return expenditure;
    }

    private int assignmentCalculate(Base base,String equipmentType, LocalDate date){
        int assignment = assignmentRepository.getAssignedQuantityOnDate(base,equipmentType,date);
        return  assignment;
    }
    private User getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isBaseCommander(User user) {

        return user.getRole().equals("BASE_COMMANDER") || user.getRole().equals("ROLE_BASE_COMMANDER");
    }

}
