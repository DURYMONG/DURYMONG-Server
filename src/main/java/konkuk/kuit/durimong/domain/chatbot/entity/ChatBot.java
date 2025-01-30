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
public class ChatBot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatBotId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slogan;

    @Column(nullable = false)
    private String mbti;

    @Column(nullable = false)
    private String accent;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String image;

}
