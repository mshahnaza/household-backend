package org.example.householdbackend.controllers;

import org.example.householdbackend.dto.request.BillCreateRequest;
import org.example.householdbackend.dto.request.BillPayRequest;
import org.example.householdbackend.dto.response.BillResponse;
import org.example.householdbackend.services.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BillController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    private BillCreateRequest billCreateRequest;
    private BillResponse billResponse;
    private BillResponse paidBillResponse;

    @BeforeEach
    public void init() {
        billCreateRequest = BillCreateRequest.builder()
                .title("Electricity Bill")
                .description("Monthly electricity payment")
                .amount(new BigDecimal("150.50"))
                .currency("USD")
                .groupId(1L)
                .assignedUserId(1L)
                .build();

        billResponse = BillResponse.builder()
                .id(1L)
                .title("Electricity Bill")
                .description("Monthly electricity payment")
                .amount(new BigDecimal("150.50"))
                .currency("USD")
                .status("PENDING")
                .assignedUserId(1L)
                .groupId(1L)
                .build();

        paidBillResponse = BillResponse.builder()
                .id(1L)
                .title("Electricity Bill")
                .description("Monthly electricity payment")
                .amount(new BigDecimal("150.50"))
                .currency("USD")
                .status("PAID")
                .receiptPhotoPath("/uploads/receipts/user_1/receipt.jpg")
                .assignedUserId(1L)
                .groupId(1L)
                .build();
    }

    @Test
    public void createBill_ReturnsCreated() throws Exception {
        given(billService.createBill(ArgumentMatchers.any(BillCreateRequest.class)))
                .willReturn(billResponse);

        ResultActions response = mockMvc.perform(post("/api/bills/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(billCreateRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(billResponse.getId()))
                .andExpect(jsonPath("$.title").value(billResponse.getTitle()))
                .andExpect(jsonPath("$.amount").value(150.50))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void createBill_InvalidRequest_ReturnsBadRequest() throws Exception {
        BillCreateRequest invalidRequest = BillCreateRequest.builder()
                .title("")
                .amount(new BigDecimal("-10.00"))
                .build();

        mockMvc.perform(post("/api/bills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadBillPayed_ReturnsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "receiptPhoto",
                "receipt.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        given(billService.uploadBillPayed(ArgumentMatchers.any(BillPayRequest.class)))
                .willReturn(paidBillResponse);

        ResultActions response = mockMvc.perform(multipart("/api/bills/pay")
                .file(file)
                .param("billId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.receiptPhotoPath").exists());
    }

    @Test
    public void getBillById_ReturnsBill() throws Exception {
        given(billService.getBillById(1L))
                .willReturn(billResponse);

        ResultActions response = mockMvc.perform(get("/api/bills/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Electricity Bill"))
                .andExpect(jsonPath("$.amount").value(150.50));
    }

    @Test
    public void getBillById_NotFound_ReturnsNotFound() throws Exception {
        given(billService.getBillById(999L))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Bill not found"));

        mockMvc.perform(get("/api/bills/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllGroupBills_ReturnsBillList() throws Exception {
        List<BillResponse> groupBills = Arrays.asList(
                BillResponse.builder()
                        .id(1L)
                        .title("Electricity")
                        .amount(new BigDecimal("150.50"))
                        .currency("USD")
                        .status("PENDING")
                        .groupId(1L)
                        .build(),
                BillResponse.builder()
                        .id(2L)
                        .title("Water")
                        .amount(new BigDecimal("50.00"))
                        .currency("USD")
                        .status("PAID")
                        .groupId(1L)
                        .build()
        );

        given(billService.getAllGroupBills(1L))
                .willReturn(groupBills);

        ResultActions response = mockMvc.perform(get("/api/bills/group/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Electricity"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Water"));
    }

    @Test
    public void getAllGroupBills_EmptyList_ReturnsEmptyArray() throws Exception {
        given(billService.getAllGroupBills(999L))
                .willReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/api/bills/group/999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    public void getAllUserGroupBills_ReturnsBillList() throws Exception {
        List<BillResponse> userBills = Arrays.asList(
                BillResponse.builder()
                        .id(1L)
                        .title("Electricity")
                        .amount(new BigDecimal("150.50"))
                        .currency("USD")
                        .status("PENDING")
                        .assignedUserId(1L)
                        .groupId(1L)
                        .build()
        );

        given(billService.getAllUserGroupBills(1L))
                .willReturn(userBills);

        ResultActions response = mockMvc.perform(get("/api/bills/group/1/user")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].assignedUserId").value(1L));
    }

    @Test
    public void getPaidBillsSum_ReturnsSum() throws Exception {
        given(billService.paidBillsSum(1L))
                .willReturn(new BigDecimal("500.00"));

        ResultActions response = mockMvc.perform(get("/api/bills/group/1/sum/paid")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    public void getLeftBillsSum_ReturnsSum() throws Exception {
        given(billService.leftBillsSum(1L))
                .willReturn(new BigDecimal("300.00"));

        ResultActions response = mockMvc.perform(get("/api/bills/group/1/sum/left")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("300.00"));
    }

    @Test
    public void getTotalBillsSum_ReturnsSum() throws Exception {
        given(billService.totalBillsSum(1L))
                .willReturn(new BigDecimal("800.00"));

        ResultActions response = mockMvc.perform(get("/api/bills/group/1/sum/total")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("800.00"));
    }

    @Test
    public void deleteBill_ReturnsNoContent() throws Exception {
        doNothing().when(billService).deleteBill(1L);

        ResultActions response = mockMvc.perform(delete("/api/bills/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void createBill_InvalidCurrency_ReturnsBadRequest() throws Exception {
        BillCreateRequest invalidRequest = BillCreateRequest.builder()
                .title("Test Bill")
                .amount(new BigDecimal("100.00"))
                .currency("US")
                .groupId(1L)
                .assignedUserId(1L)
                .build();

        mockMvc.perform(post("/api/bills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadBillPayed_WithoutFile_ReturnsBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/bills/pay")
                        .param("billId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPaidBillsSum_GroupNotFound_ReturnsZero() throws Exception {
        given(billService.paidBillsSum(999L))
                .willReturn(BigDecimal.ZERO);

        ResultActions response = mockMvc.perform(get("/api/bills/group/999/sum/paid")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}