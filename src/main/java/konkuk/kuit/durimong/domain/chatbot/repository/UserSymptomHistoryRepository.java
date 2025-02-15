package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import konkuk.kuit.durimong.domain.chatbot.entity.UserSymptomsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSymptomHistoryRepository extends JpaRepository<UserSymptomsHistory,Long> {
    Optional<UserSymptomsHistory> findBySession(ChatSession session);
}
