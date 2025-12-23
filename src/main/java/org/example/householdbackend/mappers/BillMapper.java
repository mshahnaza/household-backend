package org.example.householdbackend.mappers;

import org.example.householdbackend.dto.response.BillResponse;
import org.example.householdbackend.entities.Bill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BillMapper {
    List<BillResponse> billToBillDtos(List<Bill> bills);
    BillResponse billToBillDto(Bill bill);
}
