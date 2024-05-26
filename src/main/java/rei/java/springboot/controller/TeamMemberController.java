package rei.java.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rei.java.springboot.model.TeamMember;
import rei.java.springboot.service.TeamMemberService;

import java.util.List;

/**
 * Controller for managing team members.
 * Provides RESTful endpoints for creating, retrieving, updating, and deleting team members.
 */
@RestController
@RequestMapping("/api/team")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    /**
     * Constructor-based injection of the TeamMemberService.
     * @param teamMemberService The service handling team member logic.
     */
    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    /**
     * Create a new team member.
     * @param teamMember The team member to create.
     * @return The created team member.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamMember createTeamMember(@RequestBody TeamMember teamMember) {
        return teamMemberService.saveTeamMember(teamMember);
    }

    /**
     * Retrieve all team members.
     * @return A list of all team members.
     */
    @GetMapping
    public List<TeamMember> getAllTeamMembers() {
        return teamMemberService.getAllTeamMembers();
    }

    /**
     * Retrieve a single team member by their unique identifier.
     * @param memberId The unique identifier of the team member.
     * @return The team member if found.
     */
    @GetMapping("{memberId}")
    public ResponseEntity<TeamMember> getTeamMemberByMemberId(@PathVariable("memberId") String memberId) {
        return teamMemberService.getTeamMemberByMemberId(memberId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update an existing team member.
     * @param memberId The unique identifier of the team member to update.
     * @param teamMember The updated team member data.
     * @return The updated team member if the original was found, otherwise not found.
     */
    @PutMapping("{memberId}")
    public ResponseEntity<TeamMember> updateTeamMember(@PathVariable("memberId") String memberId, @RequestBody TeamMember teamMember) {
       // Check if the team member exists
       return teamMemberService.getTeamMemberByMemberId(memberId).map(foundTeamMember -> {
            // Update the team member details
           foundTeamMember.setFirstName(teamMember.getFirstName());
           foundTeamMember.setLastName(teamMember.getLastName());
           foundTeamMember.setEmail(teamMember.getEmail());
           TeamMember updatedTeamMember = teamMemberService.updateTeamMember(foundTeamMember);

           // Return the updated team member
           return new ResponseEntity<>(updatedTeamMember, HttpStatus.OK);
        
        // If the team member does not exist, return not found
       }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a team member by their identifier.
     * @param memberId The unique identifier of the team member to delete.
     * @return A response indicating successful deletion.
     */
    @DeleteMapping("{memberId}")
    public ResponseEntity<String> deleteTeamMember(@PathVariable("memberId") String memberId) {
        teamMemberService.deleteTeamMember(memberId);
        return new ResponseEntity<>("Team member deleted successfully!", HttpStatus.OK);
    }
}

