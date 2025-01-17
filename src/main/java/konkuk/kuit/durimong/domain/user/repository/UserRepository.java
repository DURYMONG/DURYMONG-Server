package konkuk.kuit.durimong.domain.user.repository;

import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(String id);
    boolean existsByEmail(String email);

}
