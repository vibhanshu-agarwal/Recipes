package recipes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Find a user by email
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
