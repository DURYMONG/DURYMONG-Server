package konkuk.kuit.durimong.domain.mong.repository;

import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MongRepository extends JpaRepository<Mong, Long> {
    Mong save(Mong mong);
    Optional<Mong> findByUser(User user);
}
