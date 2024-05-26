package rei.java.springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.repository.TeamMemberRepository;
import rei.java.springboot.service.implementation.TeamMemberServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TeamMemberService.
 */
@ExtendWith(MockitoExtension.class)
public class TeamMemberServiceTest {

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private TeamMemberServiceImpl teamMemberService;

    private TeamMember teamMember;

    /**
     * Setup method to initialize test data before each test.
     */
    @BeforeEach
    public void setup() {
        teamMember = TeamMember.builder()
                .memberId("TM201")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .build();
    }

    /**
     * JUnit test for saveTeamMember method.
     */
    @Test
    @DisplayName("JUnit test for saveTeamMember method")
    public void givenTeamMemberObject_whenSaveTeamMember_thenReturnTeamMember() {
        // given - a team member object and mock repository behavior
        given(teamMemberRepository.findByEmail(teamMember.getEmail())).willReturn(Optional.empty());
        given(teamMemberRepository.save(teamMember)).willReturn(teamMember);

        // when - the saveTeamMember method is called
        TeamMember savedTeamMember = teamMemberService.saveTeamMember(teamMember);

        // then - the returned team member should not be null and should have a member ID
        assertThat(savedTeamMember).isNotNull();
        assertThat(savedTeamMember.getMemberId()).isNotNull();
    }

    /**
     * JUnit test for saveTeamMember method which throws exception.
     */
    @Test
    @DisplayName("JUnit test for saveTeamMember method which throws exception")
    public void givenExistingEmail_whenSaveTeamMember_thenThrowsException() {
        // given - a team member with an existing email in the repository
        given(teamMemberRepository.findByEmail(teamMember.getEmail())).willReturn(Optional.of(teamMember));

        // when - the saveTeamMember method is called with the existing email
        // then - an IllegalStateException should be thrown and the save method should never be called
        assertThrows(IllegalStateException.class, () -> teamMemberService.saveTeamMember(teamMember));
        verify(teamMemberRepository, never()).save(any(TeamMember.class));
    }

    /**
     * JUnit test for getAllTeamMembers method.
     */
    @Test
    @DisplayName("JUnit test for getAllTeamMembers method")
    public void givenTeamMemberList_whenGetAllTeamMembers_thenReturnTeamMemberList() {
        // given - a list of team members in the repository
        TeamMember anotherTeamMember = TeamMember.builder()
                .memberId("TM202")
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@example.com")
                .build();
        given(teamMemberRepository.findAll()).willReturn(List.of(teamMember, anotherTeamMember));

        // when - the getAllTeamMembers method is called
        List<TeamMember> teamMemberList = teamMemberService.getAllTeamMembers();

        // then - the returned list should not be null and should contain the correct number of team members
        assertThat(teamMemberList).isNotNull();
        assertThat(teamMemberList.size()).isEqualTo(2);
    }

    /**
     * JUnit test for getAllTeamMembers method, negative scenario.
     */
    @Test
    @DisplayName("JUnit test for getAllTeamMembers method, negative scenario")
    public void givenEmptyList_whenGetAllTeamMembers_thenReturnEmptyList() {
        // given - an empty list of team members in the repository
        given(teamMemberRepository.findAll()).willReturn(Collections.emptyList());

        // when - the getAllTeamMembers method is called
        List<TeamMember> teamMemberList = teamMemberService.getAllTeamMembers();

        // then - the returned list should be empty
        assertThat(teamMemberList).isEmpty();
    }

    /**
     * JUnit test for getTeamMemberByMemberId method.
     */
    @Test
    @DisplayName("JUnit test for getTeamMemberByMemberId method")
    public void givenMemberId_whenGetTeamMemberByMemberId_thenReturnTeamMember() {
        // given - a team member with a specific ID in the repository
        given(teamMemberRepository.findByMemberId("TM201")).willReturn(Optional.of(teamMember));

        // when - the getTeamMemberByMemberId method is called with the specific ID
        Optional<TeamMember> teamMemberOptional = teamMemberService.getTeamMemberByMemberId(teamMember.getMemberId());

        // then - the returned optional should not be null and should contain the team member
        assertThat(teamMemberOptional).isNotNull();
    }

    /**
     * JUnit test for getTeamMemberByMemberId method, negative scenario.
     */
    @Test
    @DisplayName("JUnit test for getTeamMemberByMemberId method, negative scenario")
    public void givenMemberId_whenGetTeamMemberByMemberId_thenReturnNull() {
        // given - no team member with the specific ID in the repository
        given(teamMemberRepository.findByMemberId("TM201")).willReturn(Optional.empty());

        // when - the getTeamMemberByMemberId method is called with the specific ID
        Optional<TeamMember> teamMemberOptional = teamMemberService.getTeamMemberByMemberId(teamMember.getMemberId());

        // then - the returned optional should be empty
        assertThat(teamMemberOptional).isEmpty();
    }

    /**
     * JUnit test for updateTeamMember method.
     */
    @Test
    @DisplayName("JUnit test for updateTeamMember method")
    public void givenTeamMemberObject_whenUpdateTeamMember_thenReturnUpdatedTeamMember() {
        // given - a team member object and mock repository behavior
        given(teamMemberRepository.save(teamMember)).willReturn(teamMember);
        teamMember.setEmail("jack.green@example.com");

        // when - the updateTeamMember method is called
        TeamMember updatedTeamMember = teamMemberService.updateTeamMember(teamMember);

        // then - the returned updated team member should not be null and should have the updated email
        assertThat(updatedTeamMember).isNotNull();
        assertThat(updatedTeamMember.getEmail()).isEqualTo("jack.green@example.com");
    }

    /**
     * JUnit test for deleteTeamMember method.
     */
    @Test
    @DisplayName("JUnit test for deleteTeamMember method")
    public void givenMemberId_whenDeleteTeamMember_thenDoNothing() {
        // given - a team member ID and mock repository behavior for deleting
        String memberId = "TM201";
        willDoNothing().given(teamMemberRepository).deleteByMemberId(memberId);

        // when - the deleteTeamMember method is called with the member ID
        teamMemberService.deleteTeamMember(memberId);

        // then - verify that the deleteByMemberId method is called only once
        verify(teamMemberRepository, times(1)).deleteByMemberId(memberId);
    }
}



