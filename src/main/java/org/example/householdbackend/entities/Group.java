package org.example.householdbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "household_groups")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(name = "invitation_code", unique = true, length = 8)
    private String invitationCode;

    private LocalDateTime inviteCodeExpirationTime;

    @OneToMany(mappedBy = "group")
    private List<GroupMembership> members;

    @OneToMany(mappedBy = "group")
    private List<Task> tasks;

    @OneToMany(mappedBy = "group")
    private List<Bill> bills;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;
}
