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
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "chatBotId")
    private ChatBot chatBot;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
