package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import konkuk.kuit.durimong.domain.chatbot.entity.DiaryRecommendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRecommendHistoryRepository extends JpaRepository<DiaryRecommendHistory, Long> {
    Optional<DiaryRecommendHistory> findBySession(ChatSession session);
}
