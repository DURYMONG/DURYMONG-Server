package konkuk.kuit.durimong.domain.mong.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MongImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mongImageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private String mongType;

    @Column(nullable = false)
    private String mongColor;
}
