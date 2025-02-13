package konkuk.kuit.durimong.domain.chatbot.service;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotSessionService {
    private final ChatSessionRepository chatSessionRepository;

    @Scheduled(cron = "0 0 * * * ?")
    public void cleanUncompletedSessions(){
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<ChatSession> expiredSessions = chatSessionRepository.findUncompletedSessions(oneHourAgo);
        if (!expiredSessions.isEmpty()) {
            chatSessionRepository.deleteAll(expiredSessions);
            log.info("⏳ 미완료된 {}개 세션 삭제 완료", expiredSessions.size());
        }
    }
}
