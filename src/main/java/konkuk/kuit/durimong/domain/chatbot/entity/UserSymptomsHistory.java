package konkuk.kuit.durimong.domain.chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserSymptomsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symptoms_id")
    private Long symptomsId;

    @ElementCollection
    @CollectionTable(name = "user_symptom_choices",
                     joinColumns = @JoinColumn(name = "symptoms_id"))
    @Column(nullable = false)
    private List<String> symptoms;

    @Column(nullable = false, name = "additional_symptom")
    private String additionalSymptom;

    @Column(nullable = false, name = "bot_greeting")
    private String botGreeting;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private ChatSession session;

}
