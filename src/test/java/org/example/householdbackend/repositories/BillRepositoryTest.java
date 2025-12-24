package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Bill;
import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
public class BillRepositoryTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Group group1;
    private Group group2;

    @BeforeEach
    void setUp() {
        billRepository.deleteAll();

        user1 = User.builder()
                .email("test1@example.com")
                .username("test1")
                .password("password1")
                .build();

        user2 = User.builder()
                .email("test2@example.com")
                .username("test2")
                .password("password2")
                .build();

        user1 = entityManager.persist(user1);
        user2 = entityManager.persist(user2);

        group1 = Group.builder()
                .name("Group1")
                .description("Description1")
                .createdBy(user1)
                .build();

        group2 = Group.builder()
                .name("Group2")
                .description("Description2")
                .createdBy(user1)
                .build();

        group1 = entityManager.persist(group1);
        group2 = entityManager.persist(group2);
        entityManager.flush();
    }

    @Test
    void findAllByGroup_Id_shouldReturnAllBillsForGroup() {
        Bill bill1 = Bill.builder()
                .title("Bill1")
                .description("Description1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status("PENDING")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill1);

        Bill bill2 = Bill.builder()
                .title("Bill2")
                .description("Description2")
                .amount(new BigDecimal("200.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user2)
                .group(group1)
                .build();
        billRepository.save(bill2);

        Bill bill3 = Bill.builder()
                .title("Bill3")
                .description("Description3")
                .amount(new BigDecimal("300.00"))
                .currency("EUR")
                .status("PENDING")
                .assignedTo(user1)
                .group(group2)
                .build();
        billRepository.save(bill3);

        List<Bill> foundBills = billRepository.findAllByGroup_Id(group1.getId());

        assertEquals(2, foundBills.size());
        assertTrue(foundBills.stream().anyMatch(b -> b.getTitle().equals("Bill1")));
        assertTrue(foundBills.stream().anyMatch(b -> b.getTitle().equals("Bill2")));
        assertTrue(foundBills.stream().allMatch(b -> b.getGroup().getId().equals(group1.getId())));
    }

    @Test
    void findByAssignedTo_IdAndGroup_Id_shouldReturnSingleBill() {
        Bill bill1 = Bill.builder()
                .title("Bill1")
                .description("Description1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status("PENDING")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill1);

        Bill bill2 = Bill.builder()
                .title("Bill2")
                .description("Description2")
                .amount(new BigDecimal("200.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user2)
                .group(group1)
                .build();
        billRepository.save(bill2);

        Bill foundBill = billRepository.findByAssignedTo_IdAndGroup_Id(user1.getId(), group1.getId());

        assertNotNull(foundBill);
        assertEquals("Bill1", foundBill.getTitle());
        assertEquals(user1.getId(), foundBill.getAssignedTo().getId());
        assertEquals(group1.getId(), foundBill.getGroup().getId());
    }

    @Test
    void findAllByAssignedTo_IdAndGroup_Id_shouldReturnAllUserBillsInGroup() {
        Bill bill1 = Bill.builder()
                .title("Bill1")
                .description("Description1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status("PENDING")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill1);

        Bill bill2 = Bill.builder()
                .title("Bill2")
                .description("Description2")
                .amount(new BigDecimal("200.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill2);

        Bill bill3 = Bill.builder()
                .title("Bill3")
                .description("Description3")
                .amount(new BigDecimal("300.00"))
                .currency("EUR")
                .status("PENDING")
                .assignedTo(user2)
                .group(group1)
                .build();
        billRepository.save(bill3);

        Bill bill4 = Bill.builder()
                .title("Bill4")
                .description("Description4")
                .amount(new BigDecimal("400.00"))
                .currency("USD")
                .status("PENDING")
                .assignedTo(user1)
                .group(group2)
                .build();
        billRepository.save(bill4);

        List<Bill> foundBills = billRepository.findAllByAssignedTo_IdAndGroup_Id(user1.getId(), group1.getId());

        assertEquals(2, foundBills.size());
        assertTrue(foundBills.stream().allMatch(b -> b.getAssignedTo().getId().equals(user1.getId())));
        assertTrue(foundBills.stream().allMatch(b -> b.getGroup().getId().equals(group1.getId())));
        assertTrue(foundBills.stream().anyMatch(b -> b.getTitle().equals("Bill1")));
        assertTrue(foundBills.stream().anyMatch(b -> b.getTitle().equals("Bill2")));
    }

    @Test
    void findAllByGroup_IdAndStatus_shouldReturnBillsWithSpecificStatus() {
        Bill bill1 = Bill.builder()
                .title("Bill1")
                .description("Description1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill1);

        Bill bill2 = Bill.builder()
                .title("Bill2")
                .description("Description2")
                .amount(new BigDecimal("200.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user2)
                .group(group1)
                .build();
        billRepository.save(bill2);

        Bill bill3 = Bill.builder()
                .title("Bill3")
                .description("Description3")
                .amount(new BigDecimal("300.00"))
                .currency("EUR")
                .status("PENDING")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill3);

        Bill bill4 = Bill.builder()
                .title("Bill4")
                .description("Description4")
                .amount(new BigDecimal("400.00"))
                .currency("USD")
                .status("PAID")
                .assignedTo(user1)
                .group(group2)
                .build();
        billRepository.save(bill4);

        List<Bill> paidBills = billRepository.findAllByGroup_IdAndStatus(group1.getId(), "PAID");

        assertEquals(2, paidBills.size());
        assertTrue(paidBills.stream().allMatch(b -> b.getStatus().equals("PAID")));
        assertTrue(paidBills.stream().allMatch(b -> b.getGroup().getId().equals(group1.getId())));
        assertTrue(paidBills.stream().anyMatch(b -> b.getTitle().equals("Bill1")));
        assertTrue(paidBills.stream().anyMatch(b -> b.getTitle().equals("Bill2")));
    }

    @Test
    void findAllByGroup_Id_shouldReturnEmptyListWhenNoBills() {
        List<Bill> foundBills = billRepository.findAllByGroup_Id(group1.getId());

        assertTrue(foundBills.isEmpty());
    }

    @Test
    void findByAssignedTo_IdAndGroup_Id_shouldReturnNullWhenNoBillFound() {
        Bill foundBill = billRepository.findByAssignedTo_IdAndGroup_Id(user1.getId(), group1.getId());

        assertNull(foundBill);
    }

    @Test
    void findAllByGroup_IdAndStatus_shouldReturnEmptyListWhenNoMatchingStatus() {
        Bill bill = Bill.builder()
                .title("Bill1")
                .description("Description1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status("PENDING")
                .assignedTo(user1)
                .group(group1)
                .build();
        billRepository.save(bill);

        List<Bill> paidBills = billRepository.findAllByGroup_IdAndStatus(group1.getId(), "PAID");

        assertTrue(paidBills.isEmpty());
    }
}