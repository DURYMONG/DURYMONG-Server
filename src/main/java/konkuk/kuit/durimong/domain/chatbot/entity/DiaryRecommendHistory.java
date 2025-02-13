package konkuk.kuit.durimong.domain.chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DiaryRecommendHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_history_id")
    private Long diaryHistoryId;

    @Column(nullable = false, name = "bot_message")
    private String botMessage;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private ChatSession session;

}
