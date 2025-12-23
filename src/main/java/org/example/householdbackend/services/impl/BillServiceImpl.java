package org.example.householdbackend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.example.householdbackend.dto.request.BillCreateRequest;
import org.example.householdbackend.dto.request.BillPayRequest;
import org.example.householdbackend.dto.response.BillResponse;
import org.example.householdbackend.entities.Bill;
import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.User;
import org.example.householdbackend.mappers.BillMapper;
import org.example.householdbackend.repositories.BillRepository;
import org.example.householdbackend.repositories.GroupRepository;
import org.example.householdbackend.services.BillService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final GroupRepository groupRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public BillResponse createBill(BillCreateRequest billCreateRequest) {
        if(billCreateRequest == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }

//        User assignedTo = userRepository.findById(billCreateRequest.getAssignedUserId());
        Group group = groupRepository.findById(billCreateRequest.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        Bill bill = Bill.builder()
                .title(billCreateRequest.getTitle())
                .description(billCreateRequest.getDescription())
                .amount(billCreateRequest.getAmount())
                .currency(billCreateRequest.getCurrency())
//                .assignedTo(user)
                .group(group)
                .status("PENDING")
                .build();
        billRepository.save(bill);
        return billMapper.billToBillDto(bill);
    }

    @Override
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bill not found"));
        billRepository.delete(bill);
    }

    @Override
    public BillResponse uploadBillPayed(BillPayRequest billPayRequest) {
        if(billPayRequest == null) {
            throw new IllegalArgumentException("Bill pay cannot be null");
        }

        Bill bill = billRepository.findById(billPayRequest.getBillId())
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        MultipartFile file = billPayRequest.getReceiptPhoto();
        validateFile(file);
        String fileName = generateFileName(file);
        User assignedUser = bill.getAssignedTo();
        if (assignedUser == null) {
            throw new IllegalStateException("Bill is not assigned to any user");
        }
        Path uploadPath = Paths.get(uploadDir, "receipts", "user_" + assignedUser.getId().toString());
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.createDirectories(uploadPath);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            bill.setReceiptPhotoPath(filePath.toString());
            bill.setStatus("PAID");

            Bill updatedBill = billRepository.save(bill);
            return billMapper.billToBillDto(updatedBill);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload receipt photo", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (!isValidImageType(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }
    }

    private boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/jpg"));
    }

    private String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        return UUID.randomUUID().toString() + fileExtension;
    }

    @Override
    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bill not found"));
        return billMapper.billToBillDto(bill);
    }

    @Override
    public List<BillResponse> getAllGroupBills(Long groupId) {
        List<Bill> bills = billRepository.findAllByGroup_Id(groupId);
        return billMapper.billToBillDtos(bills);
    }

    @Override
    public List<BillResponse> getAllUserGroupBills(Long groupId) {
//        User user = userService.getCurrentUser();
//        List<Bill> bills = billRepository.findAllByAssignedTo_IdAndGroup_Id((user.getId(), groupId);
//        return billMapper.billToBillDtos(bills);
        return null;
    }

    @Override
    public BigDecimal paidBillsSum(Long groupId) {
        List<Bill> paidBills = billRepository.findAllByGroup_IdAndStatus(groupId, "PAID");

        return paidBills.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal leftBillsSum(Long groupId) {
        return totalBillsSum(groupId).subtract(paidBillsSum(groupId));
    }

    @Override
    public BigDecimal totalBillsSum(Long groupId) {
        List<Bill> totalBills = billRepository.findAllByGroup_Id(groupId);
        return totalBills.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
