package org.example.householdbackend.controllers;

import org.example.householdbackend.dto.request.GroupRequest;
import org.example.householdbackend.dto.response.GroupResponse;
import org.example.householdbackend.services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroupRequest groupRequest;
    private GroupResponse groupResponse;

    @BeforeEach
    public void init() {
        groupRequest = GroupRequest.builder()
                .name("Group 1")
                .description("First group")
                .build();

        groupResponse = GroupResponse.builder()
                .groupId(1L)
                .name("Group 1")
                .invitationCode("ABC1234DEF")
                .inviteCodeExpirationTime(LocalDateTime.now().plusDays(7))
                .description("First group")
                .build();
    }

    @Test
    public void createGroup_ReturnsCreated() throws Exception {
        given(groupService.createGroup(ArgumentMatchers.any(GroupRequest.class)))
                .willReturn(groupResponse);

        ResultActions response = mockMvc.perform(post("/api/groups/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.groupId").value(groupResponse.getGroupId()))
                .andExpect(jsonPath("$.name").value(groupResponse.getName()))
                .andExpect(jsonPath("$.description").value(groupResponse.getDescription()));
    }

    @Test
    public void createGroup_InvalidRequest_ReturnsBadRequest() throws Exception {
        GroupRequest invalidRequest = GroupRequest.builder()
                .name("")
                .build();

        mockMvc.perform(post("/api/groups/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateGroupName_ReturnsOk() throws Exception {
        given(groupService.changeGroupName(1L, "New Name"))
                .willReturn(groupResponse);

        ResultActions response = mockMvc.perform(patch("/api/groups/rename/1")
                .param("groupName", "New Name")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.name").value("Group 1"));
    }

    @Test
    public void updateGroupDescription_ReturnsOk() throws Exception {
        GroupResponse response = GroupResponse.builder()
                .groupId(1L)
                .name("Group 1")
                .description("New description")
                .build();

        given(groupService.changeGroupDescription(1L, "New description"))
                .willReturn(response);

        ResultActions responseAct = mockMvc.perform(patch("/api/groups/description/1")
                .param("description", "New description")
                .contentType(MediaType.APPLICATION_JSON));

        responseAct.andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.description").value("New description"));
    }

    @Test
    public void deleteGroup_ReturnsNoContent() throws Exception {
        doNothing().when(groupService).deleteGroup(1L);

        ResultActions response = mockMvc.perform(delete("/api/groups/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void getGroupInfo_ReturnsGroup() throws Exception {
        given(groupService.getGroupInfo(1L))
                .willReturn(groupResponse);

        ResultActions response = mockMvc.perform(get("/api/groups/info/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.name").value("Group 1"));
    }

    @Test
    public void getUsersGroups_ReturnsGroupList() throws Exception {
        List<GroupResponse> userGroups = Arrays.asList(
                GroupResponse.builder().groupId(1L).name("Квартира").build(),
                GroupResponse.builder().groupId(2L).name("Дача").build()
        );

        given(groupService.getGroupsByUserId(100L))
                .willReturn(userGroups);

        ResultActions response = mockMvc.perform(get("/api/groups/user/100")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].groupId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Квартира"))
                .andExpect(jsonPath("$[1].groupId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Дача"));
    }

    @Test
    public void generateInvitationCode_ReturnsGroupWithCode() throws Exception {
        given(groupService.createInvintationCode(1L))
                .willReturn(groupResponse);

        ResultActions response = mockMvc.perform(patch("/api/groups/invitation-code/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.invitationCode").value("ABC1234DEF"))
                .andExpect(jsonPath("$.inviteCodeExpirationTime").exists());
    }

    @Test
    public void countMembers_ReturnsMemberCount() throws Exception {
        given(groupService.countMembers(1L))
                .willReturn(5);

        ResultActions response = mockMvc.perform(get("/api/groups/members/count/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void getGroupInfo_NotFound_ReturnsNotFound() throws Exception {
        given(groupService.getGroupInfo(999L))
                .willThrow(new jakarta.persistence.EntityNotFoundException("Group not found"));

        mockMvc.perform(get("/api/groups/info/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUsersGroups_EmptyList_ReturnsEmptyArray() throws Exception {
        given(groupService.getGroupsByUserId(999L))
                .willReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/api/groups/user/999")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}