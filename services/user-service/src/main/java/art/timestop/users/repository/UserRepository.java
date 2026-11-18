package art.timestop.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import art.timestop.users.entities.User;
import art.timestop.users.enums.Role;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
    SELECT COUNT(u)
    FROM User u
    JOIN u.authorities a
    WHERE a.role = :role
    """)
    Long countAdminUsers(@Param("role") Role role);
}
