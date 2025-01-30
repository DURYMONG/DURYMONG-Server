package konkuk.kuit.durimong.domain.activity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String intro;

    @Column(nullable = true)
    private String tip;

    @Column(nullable = true)
    private String effect;
}
