package konkuk.kuit.durimong.domain.user.repository;

import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(String id);
    boolean existsByEmail(String email);
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByUserId(Long userId);
    @Query("SELECT u FROM User u WHERE u.lastLogin < :thresholdDate AND u.isPushEnabled = true")
    List<User> findUsersNotLoggedInSinceAndIsPushEnabled(LocalDateTime thresholdDate);
}
