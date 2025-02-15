package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.BotPredictionHistory;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotPredictionHistoryRepository extends JpaRepository<BotPredictionHistory, Long> {
    Optional<BotPredictionHistory> findBySession(ChatSession session);
}
