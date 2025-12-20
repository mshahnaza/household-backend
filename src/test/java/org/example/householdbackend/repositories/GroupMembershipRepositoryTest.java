package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.GroupMembership;
import org.example.householdbackend.entities.User;
import org.example.householdbackend.enums.Group_Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class GroupMembershipRepositoryTest {
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Group group1;
    private Group group2;
    private Group group3;

    @BeforeEach
    public void setUp() {
        groupMembershipRepository.deleteAll();
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

        group1 = Group.builder()
                .name("Group1")
                .description("Description1")
                .createdBy(user1)
                .build();
        groupRepository.save(group1);

        group2 = Group.builder()
                .name("Group2")
                .description("Description2")
                .createdBy(user1)
                .build();
        groupRepository.save(group2);

        group3 = Group.builder()
                .name("Group1")
                .description("Description3")
                .createdBy(user2)
                .build();
        groupRepository.save(group3);
    }

    @Test
    void findAllByGroup_Id_shouldReturnGroups() {
        GroupMembership groupMembership1 = GroupMembership.builder()
                .group(group1)
                .user(user1)
                .role(Group_Role.ROLE_ADMIN)
                .build();
        groupMembershipRepository.save(groupMembership1);

        GroupMembership groupMembership2 = GroupMembership.builder()
                .group(group1)
                .user(user1)
                .role(Group_Role.ROLE_USER)
                .build();
        groupMembershipRepository.save(groupMembership2);

        List<GroupMembership> groupMemberships = groupMembershipRepository.findAllByGroup_Id(group1.getId());
        assertEquals(groupMemberships.size(), 2);
        assertEquals(groupMemberships.get(0).getUser(), user1);
        assertEquals(groupMemberships.get(1).getUser().getId(), user1.getId());
        assertEquals(groupMemberships.get(0).getRole(), Group_Role.ROLE_ADMIN);
        assertEquals(groupMemberships.get(1).getRole(), Group_Role.ROLE_USER);
    }

    @Test
    void findByUserAndGroup_Id_shouldReturnGroups() {
        GroupMembership groupMembership1 = GroupMembership.builder()
                .group(group1)
                .user(user1)
                .role(Group_Role.ROLE_USER)
                .build();
        groupMembershipRepository.save(groupMembership1);

        GroupMembership groupMembership2 = GroupMembership.builder()
                .group(group1)
                .user(user2)
                .role(Group_Role.ROLE_USER)
                .build();
        groupMembershipRepository.save(groupMembership2);

        GroupMembership foundGroupMembership = groupMembershipRepository.findByGroup_IdAndUser_Id(group1.getId(), user1.getId());

        assertEquals(foundGroupMembership.getUser(), user1);
        assertEquals(foundGroupMembership.getRole(), Group_Role.ROLE_USER);
        assertEquals(foundGroupMembership.getGroup().getId(), group1.getId());
    }
}
