package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .email("test@example.com")
                .username("test")
                .password("test")
                .build();

        user2 = User.builder()
                .email("test2@example.com")
                .username("test2")
                .password("test2")
                .build();

        user1 = entityManager.persist(user1);
        user2 = entityManager.persist(user2);
        entityManager.flush();
        groupRepository.deleteAll();
    }

    @Test
    void findById_shouldReturnGroup() {
        Group group = Group.builder()
                .name("Group")
                .description("Description")
                .createdBy(user1)
                .build();
        groupRepository.save(group);

        Group found = groupRepository.findById(group.getId()).get();
        assertEquals(group.getId(), found.getId());
        assertEquals(group.getName(), found.getName());
        assertEquals(group.getDescription(), found.getDescription());
    }

    @Test
    void findByName_shouldReturnGroup() {
        Group group1 = Group.builder()
                .name("Group1")
                .description("Description1")
                .createdBy(user1)
                .build();
        groupRepository.save(group1);

        Group group2 = Group.builder()
                .name("Group2")
                .description("Description2")
                .createdBy(user1)
                .build();
        groupRepository.save(group2);

        Group group3 = Group.builder()
                .name("Group1")
                .description("Description3")
                .createdBy(user2)
                .build();
        groupRepository.save(group3);

        List<Group> foundByName = groupRepository.findAllByName("Group1");
        assertEquals(2, foundByName.size());
        assertEquals(group1.getId(), foundByName.get(0).getId());
        assertEquals(group3.getId(), foundByName.get(1).getId());
        assertEquals(foundByName.get(0).getName(), foundByName.get(1).getName());
    }

    @Test
    void findByCreatedBy_shouldReturnGroup() {
        Group group1 = Group.builder()
                .name("group1")
                .description("description1")
                .createdBy(user1)
                .build();
        groupRepository.save(group1);

        Group group2 = Group.builder()
                .name("group2")
                .description("description2")
                .createdBy(user1)
                .build();
        groupRepository.save(group2);

        Group group3 = Group.builder()
                .name("group3")
                .description("description3")
                .createdBy(user2)
                .build();
        groupRepository.save(group3);

        List<Group> foundByUser = groupRepository.findByCreatedBy_Id(user1.getId());
        assertEquals(2, foundByUser.size());
        assertEquals(group1.getId(), foundByUser.get(0).getId());
        assertEquals(group2.getId(), foundByUser.get(1).getId());
        assertEquals(foundByUser.get(0).getCreatedBy().getId(), foundByUser.get(1).getCreatedBy().getId());
    }

    @Test
    void findByInvitationCode_shouldReturnGroup() {
        Group group1 = Group.builder()
                .name("group1")
                .description("description1")
                .invitationCode("test123456")
                .createdBy(user1)
                .build();
        groupRepository.save(group1);

        Group group2 = Group.builder()
                .name("group2")
                .description("description2")
                .invitationCode("test223456")
                .createdBy(user1)
                .build();
        groupRepository.save(group2);

        Group found = groupRepository.findByInvitationCode("test123456").get();
        assertEquals(group1.getId(), found.getId());
        assertEquals(group1.getInvitationCode(), found.getInvitationCode());
    }
}
