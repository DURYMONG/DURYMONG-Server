package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.domain.test.entity.UserTest;
import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserTestRepository extends JpaRepository<UserTest, Long> {
    Optional<UserTest> findFirstByUserAndTestOrderByCreatedAtDesc(User user, Test test);
}
