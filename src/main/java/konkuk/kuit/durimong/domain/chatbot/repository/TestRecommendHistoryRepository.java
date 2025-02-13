package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import konkuk.kuit.durimong.domain.chatbot.entity.TestRecommendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRecommendHistoryRepository extends JpaRepository<TestRecommendHistory, Long> {
    Optional<TestRecommendHistory> findBySession(ChatSession session);
}
