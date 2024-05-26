package rei.java.springboot.service.implementation;

import org.springframework.stereotype.Service;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.repository.TeamMemberRepository;
import rei.java.springboot.service.TeamMemberService;

import java.util.List;
import java.util.Optional;

/**
 * Service layer implementation for TeamMember operations, implementing the TeamMemberService interface.
 */
@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    /**
     * Constructs the TeamMemberServiceImpl with dependency injection of TeamMemberRepository.
     * @param teamMemberRepository The repository used for database operations.
     */
    public TeamMemberServiceImpl(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Saves a new team member in the database, checking for email uniqueness.
     * @param teamMember The team member to save.
     * @return The saved team member.
     */
    @Override
    public TeamMember saveTeamMember(TeamMember teamMember) {
        Optional<TeamMember> existingMember = teamMemberRepository.findByEmail(teamMember.getEmail());
        if (existingMember.isPresent()) {
            throw new IllegalStateException("A team member already exists with the given email: " + teamMember.getEmail());
        }
        return teamMemberRepository.save(teamMember);
    }

    /**
     * Retrieves all team members from the database.
     * @return A list of team members.
     */
    @Override
    public List<TeamMember> getAllTeamMembers() {
        return teamMemberRepository.findAll();
    }

    /**
     * Retrieves a team member by their unique memberId.
     * @param memberId The memberId of the team member.
     * @return An Optional containing the found team member or empty if not found.
     */
    @Override
    public Optional<TeamMember> getTeamMemberByMemberId(String memberId) {
        return teamMemberRepository.findByMemberId(memberId);
    }

    /**
     * Updates an existing team member's details in the database.
     * @param teamMember The team member with updated information.
     * @return The updated team member.
     */
    @Override
    public TeamMember updateTeamMember(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    /**
     * Deletes a team member from the database by their memberId.
     * @param memberId The unique memberId of the team member to delete.
     */
    @Override
    public void deleteTeamMember(String memberId) {
        teamMemberRepository.findByMemberId(memberId)
            .ifPresent(teamMember -> teamMemberRepository.delete(teamMember));
    }
}

