package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findBySessionId(Long sessionId);
}
