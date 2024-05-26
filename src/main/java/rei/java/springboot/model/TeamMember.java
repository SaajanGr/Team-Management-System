package rei.java.springboot.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing a team member in the database.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "team_member")
public class TeamMember {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;
}


