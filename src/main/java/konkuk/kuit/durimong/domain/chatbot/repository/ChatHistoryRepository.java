package konkuk.kuit.durimong.domain.chatbot.repository;

import konkuk.kuit.durimong.domain.chatbot.entity.ChatBot;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatHistory;
import konkuk.kuit.durimong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    void deleteAllByUser(User user);
    List<ChatHistory> findAllByUser(User user);

    @Query("SELECT DISTINCT c.chatBot FROM ChatHistory c WHERE FUNCTION('DATE', c.createdAt) = :date")
    List<ChatBot> findChatBotsByDate(@Param("date") LocalDate date);

    @Query("SELECT c FROM ChatHistory c WHERE FUNCTION('DATE',c.createdAt) = :date and c.user = :user and c.chatBot = :chatBot")
    List<ChatHistory> findChatHistoryByUserAndChatBotAndDate(User user, ChatBot chatBot, @Param("date") LocalDate date);
}
