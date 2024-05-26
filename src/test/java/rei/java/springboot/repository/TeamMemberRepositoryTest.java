package rei.java.springboot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rei.java.springboot.model.TeamMember;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the TeamMemberRepository to verify database operations.
 */
@DataJpaTest
public class TeamMemberRepositoryTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private TeamMember teamMember;

    /**
     * Setup method to initialize a test TeamMember before each test.
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
     * Test for saving a TeamMember.
     */
    @Test
    @DisplayName("JUnit test for save team member operation")
    public void givenTeamMember_whenSave_thenReturnSavedTeamMember() {
        // when - action or behavior that we are going to test
        TeamMember savedTeamMember = teamMemberRepository.save(teamMember);

        // then - verify the output
        assertThat(savedTeamMember).isNotNull();
        assertThat(savedTeamMember.getMemberId()).isEqualTo("TM201");
    }

    /**
     * Test for finding all TeamMembers.
     */
    @Test
    @DisplayName("JUnit test for find all team members operation")
    public void givenTeamMemberList_whenFindAll_thenReturnTeamMemberList() {
        // given - precondition or setup
        TeamMember anotherTeamMember = TeamMember.builder()
                .memberId("TM202")
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@example.com")
                .build();

        teamMemberRepository.save(teamMember);
        teamMemberRepository.save(anotherTeamMember);

        // when - action or behavior that we are going to test
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();

        // then - verify the output
        assertThat(teamMemberList).isNotNull();
        assertThat(teamMemberList.size()).isEqualTo(2);
    }

    /**
     * Test for finding a TeamMember by member ID.
     */
    @Test
    @DisplayName("JUnit test for find team member by id operation")
    public void givenTeamMember_whenFindById_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        // when - action or behavior that we are going to test
        Optional<TeamMember> foundTeamMember = teamMemberRepository.findByMemberId(teamMember.getMemberId());

        // then - verify the output
        assertThat(foundTeamMember).isPresent();
        assertThat(foundTeamMember.get()).isNotNull();
    }

    /**
     * Test for finding a TeamMember by email.
     */
    @Test
    @DisplayName("JUnit test for find team member by email operation")
    public void givenTeamMember_whenFindByEmail_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        // when - action or behavior that we are going to test
        Optional<TeamMember> foundTeamMember = teamMemberRepository.findByEmail(teamMember.getEmail());

        // then - verify the output
        assertThat(foundTeamMember).isPresent();
        assertThat(foundTeamMember.get()).isNotNull();
        assertThat(foundTeamMember.get().getEmail()).isEqualTo(teamMember.getEmail());
    }

    /**
     * Test for updating a TeamMember.
     */
    @Test
    @DisplayName("JUnit test for update team member operation")
    public void givenTeamMember_whenUpdateTeamMember_thenReturnUpdatedTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        Optional<TeamMember> savedTeamMember = teamMemberRepository.findByMemberId(teamMember.getMemberId());
        assertThat(savedTeamMember).isPresent();

        savedTeamMember.get().setEmail("Jack.green@example.com");
        savedTeamMember.get().setFirstName("Jack");
        savedTeamMember.get().setLastName("Green");

        // when - action or behavior that we are going to test
        TeamMember updatedTeamMember = teamMemberRepository.save(savedTeamMember.get());

        // then - verify the output
        assertThat(updatedTeamMember).isNotNull();
        assertThat(updatedTeamMember.getEmail()).isEqualTo("Jack.green@example.com");
        assertThat(updatedTeamMember.getFirstName()).isEqualTo("Jack");
        assertThat(updatedTeamMember.getLastName()).isEqualTo("Green");
    }


    /**
     * Test for deleting a TeamMember.
     */
    @Test
    @DisplayName("JUnit test for delete team member operation")
    public void givenTeamMember_whenDeleteTeamMember_thenRemoveTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        // when - action or behavior that we are going to test
        teamMemberRepository.delete(teamMember);

        // then - verify the output
        Optional<TeamMember> teamMemberOptional = teamMemberRepository.findByMemberId(teamMember.getMemberId());
        assertThat(teamMemberOptional).isEmpty();
    }

    /**
     * Test for finding a TeamMember by JPQL query (index param).
     */
    @Test
    @DisplayName("JUnit test for find team member by JPQL query (index param) operation")
    public void givenTeamMember_whenFindByCustomQuery_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);
        assertThat(teamMember.getFirstName()).isNotNull();
        assertThat(teamMember.getLastName()).isNotNull();

        // when - action or behavior that we are going to test
        TeamMember foundTeamMember = teamMemberRepository.findByJPQL(teamMember.getFirstName(), teamMember.getLastName());

        // then - verify the output
        assertThat(foundTeamMember).isNotNull();
        assertThat(foundTeamMember.getFirstName()).isEqualTo(teamMember.getFirstName());
        assertThat(foundTeamMember.getLastName()).isEqualTo(teamMember.getLastName());
    }

    /**
     * Test for finding a TeamMember by JPQL query (named param).
     */
    @Test
    @DisplayName("JUnit test for find team member by JPQL query (named param) operation")
    public void givenTeamMember_whenFindByCustomQueryNamedParam_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);
        assertThat(teamMember.getFirstName()).isNotNull();
        assertThat(teamMember.getLastName()).isNotNull();

        // when - action or behavior that we are going to test
        TeamMember foundTeamMember = teamMemberRepository.findByJPQLNamedParameters(teamMember.getFirstName(), teamMember.getLastName());

        // then - verify the output
        assertThat(foundTeamMember).isNotNull();
        assertThat(foundTeamMember.getFirstName()).isEqualTo(teamMember.getFirstName());
        assertThat(foundTeamMember.getLastName()).isEqualTo(teamMember.getLastName());
    }

    /**
     * Test for finding a TeamMember by native (index param) query.
     */
    @Test
    @DisplayName("JUnit test for find team member by native (index param) query operation")
    public void givenTeamMember_whenFindByNativeQuery_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);
        assertThat(teamMember.getFirstName()).isNotNull();
        assertThat(teamMember.getLastName()).isNotNull();

        // when - action or behavior that we are going to test
        TeamMember foundTeamMember = teamMemberRepository.findByNative(teamMember.getFirstName(), teamMember.getLastName());

        // then - verify the output
        assertThat(foundTeamMember).isNotNull();
        assertThat(foundTeamMember.getFirstName()).isEqualTo(teamMember.getFirstName());
        assertThat(foundTeamMember.getLastName()).isEqualTo(teamMember.getLastName());
    }

    /**
     * Test for finding a TeamMember by native (named param) query.
     */
    @Test
    @DisplayName("JUnit test for find team member by native (named param) query operation")
    public void givenTeamMember_whenFindByNativeQueryNamedParameters_thenReturnTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);
        assertThat(teamMember.getFirstName()).isNotNull();
        assertThat(teamMember.getLastName()).isNotNull();

        // when - action or behavior that we are going to test
        TeamMember foundTeamMember = teamMemberRepository.findByNativeNamedParameters(teamMember.getFirstName(), teamMember.getLastName());

        // then - verify the output
        assertThat(foundTeamMember).isNotNull();
        assertThat(foundTeamMember.getFirstName()).isEqualTo(teamMember.getFirstName());
        assertThat(foundTeamMember.getLastName()).isEqualTo(teamMember.getLastName());
    }
}
