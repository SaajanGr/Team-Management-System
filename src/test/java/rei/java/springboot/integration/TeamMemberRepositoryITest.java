package rei.java.springboot.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.repository.TeamMemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for TeamMemberRepository.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamMemberRepositoryITest extends AbstractContainerBaseTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private TeamMember teamMember;

    /**
     * Setup method to initialize test data before each test.
     */
    @BeforeEach
    public void setup() {
        teamMember = TeamMember.builder()
                .memberId("TM201")
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@domain.com")
                .build();
    }

    /**
     * JUnit test for saving a team member.
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
     * JUnit test for finding all team members.
     */
    @Test
    @DisplayName("JUnit test for find all team members operation")
    public void givenTeamMemberList_whenFindAll_thenReturnTeamMemberList() {
        // given - precondition or setup
        TeamMember anotherTeamMember = TeamMember.builder()
                .memberId("TM202")
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@domain.com")
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
     * JUnit test for finding a team member by member ID.
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
     * JUnit test for finding a team member by email.
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
     * JUnit test for updating a team member.
     */
    @Test
    @DisplayName("JUnit test for update team member operation")
    public void givenTeamMember_whenUpdateTeamMember_thenReturnUpdatedTeamMember() {
        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        // when - action or behavior that we are going to test
        Optional<TeamMember> savedTeamMember = teamMemberRepository.findByMemberId(teamMember.getMemberId());
        assertThat(savedTeamMember).isPresent();

        savedTeamMember.get().setEmail("eve.green@domain.com");
        savedTeamMember.get().setFirstName("Eve");
        savedTeamMember.get().setLastName("Green");

        TeamMember updatedTeamMember = teamMemberRepository.save(savedTeamMember.get());

        // then - verify the output
        assertThat(updatedTeamMember).isNotNull();
        assertThat(updatedTeamMember.getEmail()).isEqualTo("eve.green@domain.com");
        assertThat(updatedTeamMember.getFirstName()).isEqualTo("Eve");
        assertThat(updatedTeamMember.getLastName()).isEqualTo("Green");
    }


    /**
     * JUnit test for deleting a team member.
     */
    @Test
    @DisplayName("JUnit test for delete team member operation")
    public void givenTeamMember_whenDeleteTeamMember_thenRemoveTeamMember() {

        // given - precondition or setup
        teamMemberRepository.save(teamMember);

        // when - action or behavior that we are going to test
        teamMemberRepository.delete(teamMember);
        Optional<TeamMember> teamMemberOptional = teamMemberRepository.findByMemberId(teamMember.getMemberId());

        // then - verify the output
        assertThat(teamMemberOptional).isEmpty();
    }

    /**
     * JUnit test for finding a team member by JPQL query (index param).
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
     * JUnit test for finding a team member by JPQL query (named param).
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
     * JUnit test for finding a team member by native (index param) query.
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
     * JUnit test for finding a team member by native (named param) query.
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

