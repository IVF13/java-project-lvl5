package hexlet.code.app.repository;

import hexlet.code.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findDetailsByEmail(String email);
    Optional<User> findByEmail(String email);

}
