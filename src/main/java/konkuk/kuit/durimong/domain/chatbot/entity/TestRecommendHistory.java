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
public class TestRecommendHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_history_id")
    private Long testHistoryId;

    @Column(nullable = false, name = "bot_message")
    private String botMessage;

    @ElementCollection
    @CollectionTable(name = "recommended_tests",
                     joinColumns = @JoinColumn(name = "tests_id"))
    @Column(nullable = false, name = "recommended_tests")
    private List<String> recommendedTests;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private ChatSession session;
}
