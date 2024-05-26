package rei.java.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.repository.TeamMemberRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the TeamMemberController using TestContainers.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
public class TeamMemberControllerITest extends AbstractContainerBaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ObjectMapper objectMapper;

        // Setup and finish methods to initialize and clean up the database before and after each test
        @BeforeEach
        void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        teamMemberRepository.deleteAll();
        }

        @AfterEach
        void finish() {
        teamMemberRepository.deleteAll();
        }

        // JUnit test for creating a team member
        @Test
        @DisplayName("JUnit test for creating a team member")
        public void givenTeamMemberObject_whenCreateTeamMember_thenReturnSavedTeamMember() throws Exception {
        // given - a team member object
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM201")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .build();

        // when - a POST request is made to create a new team member
        ResultActions response = mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamMember)));

        // then - the response should be a 201 CREATED with the saved team member
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId", is(teamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(teamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(teamMember.getEmail())))
                .andDo(print());
        }

        // JUnit test for retrieving all team members
        @Test
        @DisplayName("JUnit test for retrieving all team members")
        public void givenListOfTeamMembers_whenGetAllTeamMembers_returnTeamMembersList() throws Exception {
        // given - a list of team members
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM202")
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@example.com")
                .build();
        TeamMember anotherTeamMember = TeamMember.builder()
                .memberId("TM203")
                .firstName("Carol")
                .lastName("White")
                .email("carol.white@example.com")
                .build();
        List<TeamMember> teamMemberList = new ArrayList<>(List.of(teamMember, anotherTeamMember));
        teamMemberRepository.saveAll(teamMemberList);

        // when - a GET request is made to retrieve all team members
        ResultActions response = mockMvc.perform(get("/api/team")
                .contentType(MediaType.APPLICATION_JSON));

        // then - the response should be a 200 OK with the list of team members
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(teamMemberList.size())))
                .andExpect(jsonPath("$[0].memberId", is(teamMember.getMemberId())))
                .andExpect(jsonPath("$[1].memberId", is(anotherTeamMember.getMemberId())))
                .andDo(print());
        }

        // JUnit test for retrieving a team member by ID
        @Test
        @DisplayName("JUnit test for retrieving a team member by ID")
        public void givenTeamMemberId_whenGetTeamMemberById_thenReturnTeamMemberObject() throws Exception {
        // given - a team member object is saved
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM204")
                .firstName("David")
                .lastName("Brown")
                .email("david.brown@example.com")
                .build();
        teamMemberRepository.save(teamMember);

        // when - a GET request is made to retrieve the team member by ID
        ResultActions response = mockMvc.perform(get("/api/team/{id}", teamMember.getMemberId())
                .contentType(MediaType.APPLICATION_JSON));

        // then - the response should be a 200 OK with the team member object
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId", is(teamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(teamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(teamMember.getEmail())))
                .andDo(print());
        }

        // JUnit test for retrieving a team member by invalid ID
        @Test
        @DisplayName("JUnit test for retrieving a team member by invalid ID")
        public void givenInvalidTeamMemberId_whenGetTeamMemberById_thenReturnEmpty() throws Exception {
        // given - an invalid team member ID
        String teamMemberId = "TM999";

        // when - a GET request is made to retrieve the team member by invalid ID
        ResultActions response = mockMvc.perform(get("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - the response should be a 404 NOT FOUND
        response.andExpect(status().isNotFound())
                .andDo(print());
        }

        // JUnit test for updating a team member
        @Test
        @DisplayName("JUnit test for updating a team member")
        public void givenTeamMemberObject_whenUpdateTeamMember_thenReturnUpdatedTeamMember() throws Exception {
        // given - a team member object and the updated team member object
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM205")
                .firstName("Tina")
                .lastName("Adams")
                .email("tina.adams@example.com")
                .build();
        TeamMember updatedTeamMember = TeamMember.builder()
                .memberId("TM205")
                .firstName("Mike")
                .lastName("Adams")
                .email("mike.adams@example.com")
                .build();
        teamMemberRepository.save(teamMember);

        // when - a PUT request is made to update the existing team member
        ResultActions response = mockMvc.perform(put("/api/team/{id}", teamMember.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTeamMember)));

        // then - the response should be a 200 OK with the updated team member
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId", is(updatedTeamMember.getMemberId())))
                .andExpect(jsonPath("$.firstName", is(updatedTeamMember.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedTeamMember.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedTeamMember.getEmail())))
                .andDo(print());
        }

        // JUnit test for updating a team member with invalid ID
        @Test
        @DisplayName("JUnit test for updating a team member with invalid ID")
        public void givenInvalidTeamMemberId_whenUpdateTeamMember_thenReturnEmpty() throws Exception {
        // given - an invalid team member ID and an updated team member object
        String teamMemberId = "TM999";
        TeamMember updatedTeamMember = TeamMember.builder()
                .memberId(teamMemberId)
                .firstName("George")
                .lastName("Harris")
                .email("george.harris@example.com")
                .build();

        // when - a PUT request is made to update the team member with an invalid ID
        ResultActions response = mockMvc.perform(put("/api/team/{id}", teamMemberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTeamMember)));

        // then - the response should be a 404 NOT FOUND
        response.andExpect(status().isNotFound())
                .andDo(print());
        }

        // JUnit test for deleting a team member by ID
        @Test
        @DisplayName("JUnit test for deleting a team member by ID")
        public void givenTeamMemberId_whenDeleteTeamMember_thenReturn200() throws Exception {
        // given - a team member object is saved
        TeamMember teamMember = TeamMember.builder()
                .memberId("TM206")
                .firstName("Hannah")
                .lastName("Clark")
                .email("hannah.clark@example.com")
                .build();
        teamMemberRepository.save(teamMember);

        // when - a DELETE request is made to delete the team member by ID
        ResultActions response = mockMvc.perform(delete("/api/team/{id}", teamMember.getMemberId()));

        // then - the response should be a 200 OK
        response.andExpect(status().isOk())
                .andDo(print());
        }
}
