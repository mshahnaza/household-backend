package org.example.householdbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BillResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String receiptPhotoPath;
    private String status;
    private Long assignedUserId;
    private Long groupId;
}
