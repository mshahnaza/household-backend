package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findAllByGroup_Id(Long groupId);

    Bill findByAssignedTo_IdAndGroup_Id(Long assignedToId, Long groupId);

    List<Bill> findAllByAssignedTo_IdAndGroup_Id(Long assignedToId, Long groupId);

    List<Bill> findAllByGroup_IdAndStatus(Long groupId, String status);
}
