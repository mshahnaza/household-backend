package org.example.householdbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BillPayRequest {
    @NotNull(message = "Bill ID is required")
    private Long billId;

    @NotNull(message = "Receipt photo is required")
    private MultipartFile receiptPhoto;
}
