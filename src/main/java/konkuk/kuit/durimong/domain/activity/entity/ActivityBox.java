package konkuk.kuit.durimong.domain.activity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "activity_box")
public class ActivityBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_box_id")
    private Long activityBoxId;

    @Column(name = "box_name", nullable = false)
    private String boxName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String effect;

    @Column(nullable = true)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
}
