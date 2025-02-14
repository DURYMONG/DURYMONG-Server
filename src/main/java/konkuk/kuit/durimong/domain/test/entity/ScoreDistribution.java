package konkuk.kuit.durimong.domain.test.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "score_distribution")
public class ScoreDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_distribution_id")
    private Long scoreDistributionId;

    @Column(name = "start_score", nullable = false)
    private int startScore;

    @Column(name = "end_score", nullable = true)
    private Integer endScore;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
