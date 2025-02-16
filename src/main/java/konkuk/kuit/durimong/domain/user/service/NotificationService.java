package konkuk.kuit.durimong.domain.user.service;

import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final MongRepository mongRepository;
    private final FcmService fcmService;

    @Scheduled(cron = "0 0 9 * * ?")  // 매일 아침 9시에 실행
    public void sendPushNotificationsToInactiveUsers() {
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            List<User> inactiveUsers = userRepository.findUsersNotLoggedInSinceAndIsPushEnabled(sevenDaysAgo);

            if (inactiveUsers.isEmpty()) {
                log.info("📢 미접속 사용자가 없습니다. 푸시 알림을 전송하지 않습니다.");
                return;
            }

            int sentCount = 0;
            for (User user : inactiveUsers) {
                Mong mong = mongRepository.findByUser(user).orElse(null);
                assert mong != null;
                String title = mong.getName() + "이 기다리고 있어요!";
                String body = user.getId() + "님, 오랜만이에요! 두리몽과 다시 대화해보세요.";
                String fcmToken = user.getFcmToken();

                if (fcmToken != null && !fcmToken.trim().isEmpty()) {
                    fcmService.sendPushNotification(fcmToken, title, body);
                    sentCount++;
                } else {
                    log.warn("⚠️ 사용자 {} ({}) 에게 FCM 푸시 알림을 보낼 수 없습니다. (토큰 없음)", user.getUserId(), user.getId());
                }
            }

            log.info("✅ 총 {}명의 미접속 사용자에게 푸시 알림 전송 완료", sentCount);
        } catch (Exception e) {
            log.error("❌ 푸시 알림 전송 중 예외 발생: {}", e.getMessage(), e);
        }
    }
}
