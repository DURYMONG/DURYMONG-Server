package konkuk.kuit.durimong.domain.activity.repository;

import konkuk.kuit.durimong.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
