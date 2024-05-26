package rei.java.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rei.java.springboot.model.TeamMember;

import java.util.Optional;

/**
 * Repository interface for TeamMember entities, handling data operations.
 * Extends JpaRepository for CRUD operations and includes custom JPQL and SQL queries.
 */
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    
    /**
     * Finds a TeamMember by their unique memberId.
     * @param memberId The unique memberId of the TeamMember.
     * @return An Optional containing the found TeamMember or empty if not found.
     */
    @Query("SELECT t FROM TeamMember t WHERE t.memberId = ?1")
    Optional<TeamMember> findByMemberId(String memberId);

    /**
     * Finds a TeamMember by their email address.
     * @param email The email address of the TeamMember.
     * @return An Optional containing the found TeamMember or empty if not found.
     */
    @Query("SELECT t FROM TeamMember t WHERE t.email = ?1")
    Optional<TeamMember> findByEmail(String email);

    /**
     * JPQL query to find a TeamMember by memberId.
     * @param memberId The memberId of the TeamMember.
     * @return The found TeamMember.
     */
    @Query("SELECT t FROM TeamMember t WHERE t.memberId = ?1")
    TeamMember findByJPQLMemberId(String memberId);

    /**
     * JPQL query to find a TeamMember by first and last name.
     * @param firstName The first name of the TeamMember.
     * @param lastName The last name of the TeamMember.
     * @return The found TeamMember.
     */
    @Query("SELECT t FROM TeamMember t WHERE t.firstName = ?1 AND t.lastName = ?2")
    TeamMember findByJPQL(String firstName, String lastName);

    /**
     * JPQL query using named parameters to find a TeamMember by first and last name.
     * @param firstName The first name of the TeamMember.
     * @param lastName The last name of the TeamMember.
     * @return The found TeamMember.
     */
    @Query("SELECT t FROM TeamMember t WHERE t.firstName =:firstName AND t.lastName =:lastName")
    TeamMember findByJPQLNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Native SQL query to find a TeamMember by memberId.
     * @param memberId The unique memberId of the TeamMember.
     * @return The found TeamMember.
     */
    @Query(value = "SELECT * FROM team_member t WHERE t.member_id = ?1", nativeQuery = true)
    TeamMember findByNativeMemberId(String memberId);

    /**
     * Native SQL query to find a TeamMember by first and last name.
     * @param firstName The first name.
     * @param lastName The last name.
     * @return The found TeamMember.
     */
    @Query(value = "SELECT * FROM team_member t WHERE t.first_name = ?1 AND t.last_name = ?2", nativeQuery = true)
    TeamMember findByNative(String firstName, String lastName);

    /**
     * Native SQL query using named parameters to find a TeamMember by first and last name.
     * @param firstName The first name.
     * @param lastName The last name.
     * @return The found TeamMember.
     */
    @Query(value = "SELECT * FROM team_member t WHERE t.first_name =:firstName AND t.last_name =:lastName", nativeQuery = true)
    TeamMember findByNativeNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

   
    /**
     * Deletes a TeamMember by their unique memberId.
     * @param memberId The unique memberId of the TeamMember to delete.
     */
    @Modifying
    @Query("DELETE FROM TeamMember t WHERE t.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") String memberId);
}


