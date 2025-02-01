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

    @Column(nullable = false)
    private String number;

    @Column(name = "required_time", nullable = false)
    private String requiredTime;

    @Column(name = "min_option", nullable = false)
    private Integer minOption;

    @Column(name = "max_option", nullable = false)
    private Integer maxOption;

    @Column(name = "critical_score", nullable = false)
    private Integer criticalScore;
}
