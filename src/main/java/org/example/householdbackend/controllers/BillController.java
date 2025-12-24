package org.example.householdbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.BillCreateRequest;
import org.example.householdbackend.dto.request.BillPayRequest;
import org.example.householdbackend.dto.response.BillResponse;
import org.example.householdbackend.services.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillCreateRequest billCreateRequest) {
        BillResponse response = billService.createBill(billCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/pay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BillResponse> uploadBillPayed(@Valid @ModelAttribute BillPayRequest billPayRequest) {
        BillResponse response = billService.uploadBillPayed(billPayRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {
        BillResponse response = billService.getBillById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<BillResponse>> getAllGroupBills(@PathVariable Long groupId) {
        List<BillResponse> bills = billService.getAllGroupBills(groupId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/group/{groupId}/user")
    public ResponseEntity<List<BillResponse>> getAllUserGroupBills(@PathVariable Long groupId) {
        List<BillResponse> bills = billService.getAllUserGroupBills(groupId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/group/{groupId}/sum/paid")
    public ResponseEntity<BigDecimal> getPaidBillsSum(@PathVariable Long groupId) {
        BigDecimal sum = billService.paidBillsSum(groupId);
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/group/{groupId}/sum/left")
    public ResponseEntity<BigDecimal> getLeftBillsSum(@PathVariable Long groupId) {
        BigDecimal sum = billService.leftBillsSum(groupId);
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/group/{groupId}/sum/total")
    public ResponseEntity<BigDecimal> getTotalBillsSum(@PathVariable Long groupId) {
        BigDecimal sum = billService.totalBillsSum(groupId);
        return ResponseEntity.ok(sum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}