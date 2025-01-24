package konkuk.kuit.durimong.domain.user.entity;

import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMongConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMongConversationId;

    @Column(nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(name = "mongQuestionId")
    private MongQuestion question;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
