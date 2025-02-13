package konkuk.kuit.durimong.domain.chatbot.entity;

import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_history_id")
    private Long chatHistoryId;

    @Column(name = "bot_message")
    private String botMessage;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "symptomsId", nullable = false)
    private UserSymptomsHistory symptomsHistory;

    @OneToOne
    @JoinColumn(name = "predictionId")
    private BotPredictionHistory predictionHistory;

    @OneToOne
    @JoinColumn(name = "testHistoryId")
    private TestRecommendHistory testHistory;

    @OneToOne
    @JoinColumn(name = "diaryHistoryId")
    private DiaryRecommendHistory diaryHistory;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "chatBotId")
    private ChatBot chatBot;
}
