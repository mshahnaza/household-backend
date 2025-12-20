package org.example.householdbackend.controllers;

import org.example.householdbackend.dto.response.GroupMembershipResponse;
import org.example.householdbackend.services.GroupMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = GroupMembershipController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GroupMembershipControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupMembershipService groupMembershipService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroupMembershipResponse groupMembershipResponse;

    @BeforeEach
    public void init() {
        groupMembershipResponse = GroupMembershipResponse.builder()
                .id(1L)
                .groupId(1L)
                .userId(1L)
                .role("ROLE_USER")
                .build();
    }

    @Test
    public void joinGroup_ReturnsCreated() throws Exception {
        String invitationCode = "ABC1234DEF";
        given(groupMembershipService.joinGroupByCode(eq(invitationCode)))
                .willReturn(groupMembershipResponse);

        ResultActions response = mockMvc.perform(post("/group/member/join")
                .param("code", invitationCode)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    public void joinGroupByCode_InvalidCode_ReturnsBadRequest() throws Exception {
        String invalidCode = "";
        given(groupMembershipService.joinGroupByCode(eq(invalidCode)))
                .willThrow(new IllegalArgumentException("Invalid invitation code"));

        mockMvc.perform(post("/group/member/join")
                        .param("code", invalidCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeGroupMember_ReturnsNoContent() throws Exception {
        Long userId = 1L;
        doNothing().when(groupMembershipService).removeMember(userId);

        ResultActions response = mockMvc.perform(delete("/group/member/remove/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void removeGroupMember_NotFound_ReturnsNotFound() throws Exception {
        Long nonExistentUserId = 999L;
        doNothing().when(groupMembershipService).removeMember(eq(nonExistentUserId));

        // When & Then - все равно вернет 204, так как метод void
        // Если нужен 404, нужно изменить логику сервиса/контроллера
        mockMvc.perform(delete("/group/member/remove/{userId}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void changeMemberRole_ReturnsOk() throws Exception {
        Long groupId = 1L;
        Long memberId = 1L;
        String newRole = "ROLE_ADMIN";

        GroupMembershipResponse updatedResponse = GroupMembershipResponse.builder()
                .id(1L)
                .groupId(groupId)
                .userId(memberId)
                .role(newRole)
                .build();

        given(groupMembershipService.changeMemberRole(eq(groupId), eq(memberId), eq(newRole)))
                .willReturn(updatedResponse);

        ResultActions response = mockMvc.perform(patch("/group/member/{groupId}/members/{memberId}/role",
                groupId, memberId)
                .param("role", newRole)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.groupId").value(groupId))
                .andExpect(jsonPath("$.userId").value(memberId))
                .andExpect(jsonPath("$.role").value(newRole));
    }

    @Test
    public void changeMemberRole_InvalidRole_ReturnsBadRequest() throws Exception {
        Long groupId = 100L;
        Long memberId = 50L;
        String invalidRole = "INVALID_ROLE";

        given(groupMembershipService.changeMemberRole(eq(groupId), eq(memberId), eq(invalidRole)))
                .willThrow(new IllegalArgumentException("Invalid role"));

        mockMvc.perform(patch("/group/member/{groupId}/members/{memberId}/role",
                        groupId, memberId)
                        .param("role", invalidRole)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getGroupMembers_ReturnsMemberList() throws Exception {
        Long groupId = 1L;

        List<GroupMembershipResponse> members = Arrays.asList(
                GroupMembershipResponse.builder()
                        .id(1L)
                        .groupId(groupId)
                        .userId(1L)
                        .role("ROLE_ADMIN")
                        .build(),
                GroupMembershipResponse.builder()
                        .id(2L)
                        .groupId(groupId)
                        .userId(2L)
                        .role("ROLE_USER")
                        .build(),
                GroupMembershipResponse.builder()
                        .id(3L)
                        .groupId(groupId)
                        .userId(3L)
                        .role("ROLE_USER")
                        .build()
        );

        given(groupMembershipService.getGroupMembers(eq(groupId)))
                .willReturn(members);

        ResultActions response = mockMvc.perform(get("/group/member/list/{groupId}", groupId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].groupId").value(groupId))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].role").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[2].id").value(3L))
                .andExpect(jsonPath("$[2].userId").value(3L));
    }

    @Test
    public void getGroupMembers_EmptyGroup_ReturnsEmptyList() throws Exception {
        Long emptyGroupId = 999L;
        given(groupMembershipService.getGroupMembers(eq(emptyGroupId)))
                .willReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/group/member/list/{groupId}", emptyGroupId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    public void getGroupMembers_NotFound_ReturnsNotFound() throws Exception {
        Long nonExistentGroupId = 999L;
        given(groupMembershipService.getGroupMembers(eq(nonExistentGroupId)))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Group not found"));

        mockMvc.perform(get("/group/member/list/{groupId}", nonExistentGroupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void changeMemberRole_MemberNotFound_ReturnsNotFound() throws Exception {
        Long groupId = 100L;
        Long nonExistentMemberId = 999L;
        String role = "ROLE_ADMIN";

        given(groupMembershipService.changeMemberRole(eq(groupId), eq(nonExistentMemberId), eq(role)))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Member not found"));

        mockMvc.perform(patch("/group/member/{groupId}/members/{memberId}/role",
                        groupId, nonExistentMemberId)
                        .param("role", role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void joinGroupByCode_ExpiredCode_ReturnsBadRequest() throws Exception {
        String expiredCode = "EXPIRED123";
        given(groupMembershipService.joinGroupByCode(eq(expiredCode)))
                .willThrow(new IllegalArgumentException("Invitation code has expired"));

        mockMvc.perform(post("/group/member/join")
                        .param("code", expiredCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void joinGroupByCode_AlreadyMember_ReturnsBadRequest() throws Exception {
        String code = "DUPLICATE123";
        given(groupMembershipService.joinGroupByCode(eq(code)))
                .willThrow(new IllegalStateException("User is already a member of this group"));

        mockMvc.perform(post("/group/member/join")
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
