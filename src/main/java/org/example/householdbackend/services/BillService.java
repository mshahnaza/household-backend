package org.example.householdbackend.services;

import org.example.householdbackend.dto.request.BillCreateRequest;
import org.example.householdbackend.dto.request.BillPayRequest;
import org.example.householdbackend.dto.response.BillResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BillService {
    BillResponse createBill(BillCreateRequest billCreateRequest);
    void deleteBill(Long id);
    BillResponse uploadBillPayed(BillPayRequest billPayRequest);
    BillResponse getBillById(Long id);
    List<BillResponse> getAllGroupBills(Long groupId);
    List<BillResponse> getAllUserGroupBills(Long groupId);
    BigDecimal paidBillsSum(Long groupId);
    BigDecimal leftBillsSum(Long groupId);
    BigDecimal totalBillsSum(Long groupId);
}
