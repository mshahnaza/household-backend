package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findById(Long id);
    Optional<Group> findByName(String name);
    List<Group> findByUserId(Long userId);
    Optional<Group> findByInvitationCode(String invitationCode);
}
