package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.GroupMembership;
import org.example.householdbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    Optional<GroupMembership> findByUser(User user);

    List<GroupMembership> findAllByGroup_Id(Long groupId);

    GroupMembership findByGroup_IdAndUser_Id(Long groupId, Long userId);
}
