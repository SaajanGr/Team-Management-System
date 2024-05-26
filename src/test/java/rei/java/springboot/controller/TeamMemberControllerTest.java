package rei.java.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.service.TeamMemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the TeamMemberController.
 */
@WebMvcTest
public class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamMemberService teamMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for creating a new team member.
     * Ensures that a new team member is created and returned with status 201 Created.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenTeamMemberObject_whenCreateTeamMember_thenReturnSavedTeamMember() throws Exception {
        // setup the test data (team member object)
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM126")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@domain.com")
                .build();

        // given - mock the service method saveTeamMember
        given(teamMemberService.saveTeamMember(any(TeamMember.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - perform the POST request to create a new team member using the mock service
        ResultActions response = mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamMember)));

        // then - verify the response status and the team member object returned
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId", is(teamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(teamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(teamMember.getEmail())))
                .andDo(print());
    }

    /*
     * Test case for retrieving all team members.
     * Ensures that the list of team members is returned with status 200 OK.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenListOfTeamMembers_whenGetAllTeamMembers_returnTeamMembersList() throws Exception {
        
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM127")
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@domain.com")
                .build();
    
        TeamMember anotherTeamMember = TeamMember.builder()
                .memberId("TM128")
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@domain.com")
                .build();
    
        List<TeamMember> teamMemberList = new ArrayList<>(List.of(teamMember, anotherTeamMember));
    
        // given - mock the service method getAllTeamMembers
        given(teamMemberService.getAllTeamMembers()).willReturn(teamMemberList);
    
        // when - perform the GET request to retrieve all team members
        ResultActions response = mockMvc.perform(get("/api/team")
                .contentType(MediaType.APPLICATION_JSON));
    
        // then - verify the response status and the list of team members returned
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].memberId", is("TM127")))
                .andExpect(jsonPath("$[1].memberId", is("TM128")))
                .andDo(print());
    }
    
    /**
     * Test case for retrieving a single team member by memberId.
     * Ensures that the correct team member is returned with status 200 OK.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenTeamMemberId_whenGetTeamMemberById_thenReturnTeamMemberObject() throws Exception {
        String teamMemberId = "TM129";
        TeamMember teamMember = TeamMember.builder()
                .memberId(teamMemberId)
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie.brown@domain.com")
                .build();
    
        // given - mock the service method getTeamMemberByMemberId
        given(teamMemberService.getTeamMemberByMemberId(teamMemberId)).willReturn(Optional.of(teamMember));
    
        // when - perform the GET request to retrieve the team member
        ResultActions response = mockMvc.perform(get("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON));
    
        // then - verify the response status and the team member object returned
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId", is(teamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(teamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(teamMember.getEmail())))
                .andDo(print());
    }
    
    // additional test for invalid email
    @Test
    public void givenInvalidEmail_whenCreateTeamMember_thenReturnBadRequest() throws Exception {
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM130")
                .firstName("Dana")
                .lastName("White")
                .email("invalid-email")
                .build();
        // given - mock the service method saveTeamMember
        ResultActions response = mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamMember)));
        // then - verify the response status
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Test case for retrieving a team member by an invalid memberId.
     * Ensures that the response status is 404 NOT FOUND.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenInvalidTeamMemberId_whenGetTeamMemberById_thenReturnEmpty() throws Exception {
        String teamMemberId = "TM999";

        // given - mock the service method getTeamMemberByMemberId
        given(teamMemberService.getTeamMemberByMemberId(teamMemberId)).willReturn(Optional.empty());

        // when - perform the GET request to retrieve the team member
        ResultActions response = mockMvc.perform(get("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the response status
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Test case for updating an existing team member.
     * Ensures that the updated team member is returned with status 200 OK.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenTeamMemberObject_whenUpdateTeamMember_thenReturnUpdatedTeamMember() throws Exception {
        String teamMemberId = "TM131";
        TeamMember teamMember = TeamMember.builder()
                .memberId(teamMemberId)
                .firstName("Eva")
                .lastName("Green")
                .email("eva.green@domain.com")
                .build();

        // create an updated team member object
        TeamMember updatedTeamMember = TeamMember.builder()
                .memberId(teamMemberId)
                .firstName("Tina")
                .lastName("Greene")
                .email("tina.greene@domain.com")
                .build();

        // given - mock the service method getTeamMemberByMemberId and updateTeamMember
        given(teamMemberService.getTeamMemberByMemberId(teamMemberId)).willReturn(Optional.of(teamMember));
        given(teamMemberService.updateTeamMember(any(TeamMember.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // when - perform the PUT request to update the team member
        ResultActions response = mockMvc.perform(put("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTeamMember)));

        // then - verify the response status and the updated team member object
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId", is(updatedTeamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(updatedTeamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedTeamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedTeamMember.getEmail())))
                .andDo(print());
    }

    /**
     * Test case for updating a team member with an invalid memberId.
     * Ensures that the response status is 404 NOT FOUND.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenInvalidTeamMemberId_whenUpdateTeamMember_thenReturnEmpty() throws Exception {
        String teamMemberId = "TM999";

        TeamMember updatedTeamMember = TeamMember.builder()
                .memberId(teamMemberId)
                .firstName("Gina")
                .lastName("Carano")
                .email("gina.carano@domain.com")
                .build();

        // given - mock the service method getTeamMemberByMemberId and updateTeamMember
        given(teamMemberService.getTeamMemberByMemberId(teamMemberId)).willReturn(Optional.empty());
        given(teamMemberService.updateTeamMember(any(TeamMember.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // when - perform the PUT request to update the team member
        ResultActions response = mockMvc.perform(put("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTeamMember)));

        // then - verify the response status
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Test case for deleting an existing team member by memberId.
     * Ensures that the response status is 200 OK.
     * @throws Exception in case of any errors during the test.
     */
    @Test
    public void givenTeamMemberId_whenDeleteTeamMember_return200() throws Exception {
        String teamMemberId = "TM132";

        // given - mock the service method deleteTeamMember
        willDoNothing().given(teamMemberService).deleteTeamMember(teamMemberId);

        // when - perform the DELETE request to delete the team member
        ResultActions response = mockMvc.perform(delete("/api/team/{id}", teamMemberId));

        // then - verify the response status
        response.andExpect(status().isOk())
                .andDo(print());
    }
}



