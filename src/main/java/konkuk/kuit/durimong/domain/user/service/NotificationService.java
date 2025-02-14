package konkuk.kuit.durimong.domain.user.service;

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
    private final FcmService fcmService;

    @Scheduled(cron = "0 0 9 * * ?")  // 매일 아침 9시에 실행
    public void sendPushNotificationsToInactiveUsers() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<User> inactiveUsers = userRepository.findUsersNotLoggedInSince(sevenDaysAgo);

        for (User user : inactiveUsers) {
            String title = "두리몽이 기다리고 있어요!";
            String body = user.getNickname() + "님, 오랜만이에요! 두리몽과 다시 대화해보세요.";
            String fcmToken = user.getFcmToken();

            if (fcmToken != null) {
                fcmService.sendPushNotification(fcmToken, title, body);
            }
        }
        log.info("✅ {}명의 미접속 사용자에게 푸시 알림 전송 완료", inactiveUsers.size());
    }
}
