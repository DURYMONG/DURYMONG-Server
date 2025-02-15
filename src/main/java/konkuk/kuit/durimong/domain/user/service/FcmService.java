package konkuk.kuit.durimong.domain.user.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

import static konkuk.kuit.durimong.global.exception.ErrorCode.INVALID_FCM_TOKEN;
import static konkuk.kuit.durimong.global.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserRepository userRepository;

    public void sendPushNotification(String token, String title, String body) {
        if (token == null || token.trim().isEmpty()) {
            log.error("token is null or empty");
        }
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("✅ 푸시 알림 전송 성공: {}", response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("❌ 푸시 알림 전송 실패", e);
        }
    }

    @Transactional
    public void updateFcmToken(Long userId, String fcmToken) {
        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            throw new CustomException(INVALID_FCM_TOKEN);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }
}
