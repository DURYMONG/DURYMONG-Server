package konkuk.kuit.durimong.domain.test.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String englishName;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "count_of_questions", nullable = false)
    private int countOfQuestions;

    @Column(name = "required_time", nullable = false)
    private int requiredTime;

    @Column(name = "min_number", nullable = false)
    private Integer minNumber;

    @Column(name = "max_number", nullable = false)
    private Integer maxNumber;

    @Column(name = "critical_score", nullable = false)
    private Integer criticalScore;
}
