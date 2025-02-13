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
public class BotPredictionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Long predictionId;

    @Column(nullable = false, name = "bot_message")
    private String botMessage;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private ChatSession session;
}
