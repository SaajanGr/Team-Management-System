package rei.java.springboot.service;

import rei.java.springboot.model.TeamMember;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing team members.
 */
public interface TeamMemberService {

    /**
     * Saves a new team member to the database.
     * @param teamMember The team member to save.
     * @return The saved team member.
     */
    TeamMember saveTeamMember(TeamMember teamMember);

    /**
     * Retrieves all team members from the database.
     * @return A list of all team members.
     */
    List<TeamMember> getAllTeamMembers();

    /**
     * Retrieves a team member by their unique memberId.
     * @param memberId The unique identifier of the team member.
     * @return An Optional containing the team member if found.
     */
    Optional<TeamMember> getTeamMemberByMemberId(String memberId);

    /**
     * Updates the details of an existing team member.
     * @param teamMember The team member with updated details.
     * @return The updated team member.
     */
    TeamMember updateTeamMember(TeamMember teamMember);

    /**
     * Deletes a team member from the database using their memberId.
     * @param memberId The memberId of the team member to delete.
     */
    void deleteTeamMember(String memberId);
}
