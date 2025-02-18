package konkuk.kuit.durimong.domain.mong.repository;

import konkuk.kuit.durimong.domain.mong.entity.MongImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MongImageRepository extends JpaRepository<MongImage, Long> {
    Optional<MongImage> findByMongTypeAndMongColorAndLevel(String mongType, String mongColor, int level);
}
