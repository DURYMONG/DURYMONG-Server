package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findBySessionId(Long sessionId);
    @Query("SELECT s FROM ChatSession s WHERE s.isCompleted = false AND s.createdAt <= :time")
    List<ChatSession> findUncompletedSessions(@Param("time") LocalDateTime time);
}
