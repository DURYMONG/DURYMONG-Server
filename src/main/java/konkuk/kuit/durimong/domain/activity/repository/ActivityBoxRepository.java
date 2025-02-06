package konkuk.kuit.durimong.domain.activity.repository;

import konkuk.kuit.durimong.domain.activity.entity.Activity;
import konkuk.kuit.durimong.domain.activity.entity.ActivityBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityBoxRepository extends JpaRepository<ActivityBox, Long> {
    List<ActivityBox> findByActivity(Activity activity);
    boolean existsByActivity(Activity activity);
}
