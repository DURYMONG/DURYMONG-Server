package konkuk.kuit.durimong.domain.activity.repository;

import konkuk.kuit.durimong.domain.activity.dto.response.ActivityRecordRes;
import konkuk.kuit.durimong.domain.activity.entity.UserRecord;
import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
    boolean existsByUser_UserIdAndActivity_ActivityId(Long userId, Long activityId);

    // 활동이 있는 날짜만 조회 (활동을 해야 UserRecord가 생기므로)
    @Query("SELECT ur.createdAt, COUNT(ur) " +
            "FROM UserRecord ur " +
            "WHERE ur.user.userId = :userId " +
            "AND FUNCTION('YEAR', ur.createdAt) = :year " +
            "AND FUNCTION('MONTH', ur.createdAt) = :month " +
            "GROUP BY ur.createdAt")
    List<Object[]> findActivityCountByMonth(@Param("userId") Long userId,
                                            @Param("year") int year,
                                            @Param("month") int month);

}

