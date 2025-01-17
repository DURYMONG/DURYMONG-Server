package konkuk.kuit.durimong.domain.bot.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long botId;

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

    @Builder.Default
    private boolean isBookMarked = false;
}
