package com.example.militaryassetmanagement.service;


import com.example.militaryassetmanagement.entity.*;
import com.example.militaryassetmanagement.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MilitaryAssetService {

    private final PurchaseRepository purchaseRepository;
    private final TransferRepository transferRepository;
    private final AssignmentRepository assignmentRepository;
    private final ExpenditureRepository expenditureRepository;

    private final BaseRepository baseRepository;
    private final EquipmentRepository equipmentRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;


    public MilitaryAssetService(
            PurchaseRepository purchaseRepository,
            TransferRepository transferRepository,
            AssignmentRepository assignmentRepository,
            ExpenditureRepository expenditureRepository,
            BaseRepository baseRepository,
            EquipmentRepository equipmentRepository,
            AuditRepository auditRepository,UserRepository userRepository) {

        this.purchaseRepository = purchaseRepository;
        this.transferRepository = transferRepository;
        this.assignmentRepository = assignmentRepository;
        this.expenditureRepository = expenditureRepository;

        this.baseRepository = baseRepository;
        this.equipmentRepository = equipmentRepository;
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    public Purchase addPurchase(Purchase purchase) {
        int baseId = purchase.getBase().getId();
        int equipmentId = purchase.getEquipment().getId();
        int quantity = purchase.getQuantity();

        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new RuntimeException("Base not found"));
        checkBaseAccess(base);

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity is not valid, it should be greater than zero");
        }

        purchase.setBase(base);
        purchase.setEquipment(equipment);
        purchase.setDate(LocalDate.now());

        Purchase newPurchase = purchaseRepository.save(purchase);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Audit audit = new Audit();

        audit.setUsername(username);
        audit.setAction("PURCHASE");
        audit.setDetails(quantity + " " + equipment.getName() + " purchased for " + base.getName()
        );
        audit.setTimestamp(LocalDateTime.now());

        auditRepository.save(audit);

        return newPurchase;
    }

    public List<Purchase> getAllPurchases() {

        List<Purchase> purchases =
                purchaseRepository.findAll();

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return purchases;
        }

        int baseId = user.getBase().getId();

        return purchases.stream()
                .filter(p ->
                        p.getBase() != null &&
                                p.getBase().getId() == baseId
                )
                .toList();
    }

    public Transfer addTransfer(Transfer transfer){
        int sourceBaseId = transfer.getSourceBase().getId();
        int destinationBaseId = transfer.getDestinationBase().getId();
        int equipmentId = transfer.getEquipment().getId();
        int quantity = transfer.getQuantity();


        Base sourceBase = baseRepository.findById(sourceBaseId)
                        .orElseThrow(() -> new RuntimeException("Source Base not Found"));

        Base destinationBase = baseRepository.findById(destinationBaseId)
                .orElseThrow(() -> new RuntimeException("Destination Base not found"));
        checkTransferAccess(
                sourceBase,
                destinationBase
        );

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not Found"));

        if(sourceBaseId == destinationBaseId){
            throw new RuntimeException("sourceBase and destination base is same it should be different");
        }
        if(quantity <= 0){
            throw new RuntimeException("quantity is not valid, It should be greater than zero");
        }

        int availableQuantity = getAvailableQuantity(sourceBase, equipment);

        if(availableQuantity < quantity){

            throw new RuntimeException("available stock is less than quantity mentioned");

        }
            transfer.setSourceBase(sourceBase);
            transfer.setDestinationBase(destinationBase);
            transfer.setEquipment(equipment);
            transfer.setDate(LocalDateTime.now());
            Transfer newTransfer =  transferRepository.save(transfer);



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

         Audit audit = new Audit();

        audit.setUsername(username);
        audit.setAction("TRANSFER");
        audit.setDetails( quantity + " " + equipment.getName() + " transferred from " + sourceBase.getName()
                         + " to "
                        + destinationBase.getName()
        );
       audit.setTimestamp(LocalDateTime.now());


        auditRepository.save(audit);
        return  newTransfer;
    }


    public List<Transfer> getAllTransfers() {

        List<Transfer> transfers =
                transferRepository.findAll();

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return transfers;
        }

        int baseId = user.getBase().getId();

        return transfers.stream()
                .filter(t ->
                        (t.getSourceBase() != null &&
                                t.getSourceBase().getId() == baseId)
                                ||
                                (t.getDestinationBase() != null &&
                                        t.getDestinationBase().getId() == baseId)
                )
                .toList();
    }

    public Assignment addAssignment(Assignment assignment){
         int baseId = assignment.getBase().getId();
         int equipmentId = assignment.getEquipment().getId();
         int quantity = assignment.getQuantity();

         Base base = baseRepository.findById(baseId)
                 .orElseThrow(() -> new RuntimeException("Base Not found"));
        checkBaseAccess(base);

         Equipment equipment = equipmentRepository.findById(equipmentId)
                 .orElseThrow(() -> new RuntimeException("Equipment not found"));

        int availableQuantity = getAvailableQuantity(base, equipment);

        if(quantity <= 0){
            throw new RuntimeException("quantity is not valid, It should be greater than zero");
        }

        if(quantity > availableQuantity){
            throw new RuntimeException("available stock is less than quantity mentioned");
        }

        assignment.setBase(base);
        assignment.setEquipment(equipment);
        assignment.setDate(LocalDate.now());

        Assignment newAssignment =  assignmentRepository.save(assignment);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        Audit audit = new Audit();

        audit.setUsername(username);
        audit.setAction("ASSIGNMENT");
        audit.setDetails(quantity + " " + equipment.getName() + " assigned from " + base.getName() + " to "
                        + assignment.getPersonName()
        );
        audit.setTimestamp(LocalDateTime.now());


        auditRepository.save(audit);
        return  newAssignment;

    }

    public List<Assignment> getAllAssignments() {

        List<Assignment> assignments =
                assignmentRepository.findAll();

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return assignments;
        }

        int baseId = user.getBase().getId();

        return assignments.stream()
                .filter(a ->
                        a.getBase() != null &&
                                a.getBase().getId() == baseId
                )
                .toList();
    }

    public Expenditure addExpenditure(Expenditure expenditure){

        int baseId = expenditure.getBase().getId();
        int equipmentId = expenditure.getEquipment().getId();
        int quantity = expenditure.getQuantity();

        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new RuntimeException("Base not found"));
        checkBaseAccess(base);

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        if(quantity <= 0){
            throw new RuntimeException("quantity is not valid, It should be greater than zero");
        }

        int availableQuantity = getAvailableQuantity(base, equipment);

        if(availableQuantity < quantity){
            throw new RuntimeException("available stock is less than quantity mentioned");
        }

        expenditure.setBase(base);
        expenditure.setEquipment(equipment);
        expenditure.setDate(LocalDate.now());

        Expenditure newexpenditure = expenditureRepository.save(expenditure);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        Audit audit = new Audit();

        audit.setUsername(username);
        audit.setAction("EXPENDITURE");
        audit.setDetails(quantity + " " + equipment.getName() + " expended from " + base.getName());
        audit.setTimestamp(LocalDateTime.now());


        auditRepository.save(audit);
        return  newexpenditure;

    }

    public List<Expenditure> getAllExpenditures() {

        List<Expenditure> expenditures =
                expenditureRepository.findAll();

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return expenditures;
        }

        int baseId = user.getBase().getId();

        return expenditures.stream()
                .filter(e ->
                        e.getBase() != null &&
                                e.getBase().getId() == baseId
                )
                .toList();
    }

    private int getAvailableQuantity(Base base, Equipment equipment) {

        int purchased =
                purchaseRepository.getPurchasedQuantity(base, equipment);

        int transferIn =
                transferRepository.getTransferInQuantity(base, equipment);

        int transferOut =
                transferRepository.getTransferOutQuantity(base, equipment);

        int assigned =
                assignmentRepository.getAssignedQuantity(base, equipment);

        int expended =
                expenditureRepository.getExpendedQuantity(base, equipment);

        return purchased
                + transferIn
                - transferOut
                - assigned
                - expended;
    }
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    private boolean isBaseCommander(User user) {

        return user.getRole().equals("BASE_COMMANDER")
                || user.getRole().equals("ROLE_BASE_COMMANDER");
    }

    private void checkBaseAccess(Base base) {

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return;
        }

        if (user.getBase() == null) {
            throw new RuntimeException(
                    "Base Commander has no assigned base"
            );
        }

        if (base.getId() != user.getBase().getId()) {
            throw new RuntimeException(
                    "You can access only your assigned base"
            );
        }
    }

    private void checkTransferAccess(
            Base sourceBase,
            Base destinationBase) {

        User user = getLoggedInUser();

        if (!isBaseCommander(user)) {
            return;
        }

        if (user.getBase() == null) {
            throw new RuntimeException(
                    "Base Commander has no assigned base"
            );
        }

        int userBaseId = user.getBase().getId();

        if (sourceBase.getId() != userBaseId
                && destinationBase.getId() != userBaseId) {

            throw new RuntimeException(
                    "You can transfer only for your assigned base"
            );
        }
    }

}
