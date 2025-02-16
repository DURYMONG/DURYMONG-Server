package konkuk.kuit.durimong.domain.activity.repository;

import konkuk.kuit.durimong.domain.activity.entity.UserRecord;
import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
    boolean existsByUser_UserIdAndActivity_ActivityIdAndCreatedAt(Long userId, Long activityId, LocalDate createdAt);

    Optional<UserRecord> findByActivity_ActivityIdAndCreatedAtAndUser_UserId(Long activityId, LocalDate createdAt, Long userId);

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
    @Query("SELECT ur.createdAt " +
            "FROM UserRecord ur " +
            "WHERE ur.user = :user " +
            "AND ur.createdAt >= :startDate " +
            "GROUP BY ur.createdAt " +
            "HAVING COUNT(DISTINCT ur.activity.activityId) >= 3")
    List<LocalDate> findDaysWithThreeOrMoreActivities(@Param("user") User user, @Param("startDate") LocalDate startDate);
}

