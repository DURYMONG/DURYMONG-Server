package konkuk.kuit.durimong.domain.activity.repository;

import konkuk.kuit.durimong.domain.activity.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
}
