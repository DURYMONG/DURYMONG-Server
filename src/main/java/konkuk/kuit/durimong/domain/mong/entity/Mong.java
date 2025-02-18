package konkuk.kuit.durimong.domain.mong.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonDeserialize(builder = Mong.MongBuilder.class)
public class Mong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MongId;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    private int level = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate lastGrowthDate;

    @OneToOne
    @JoinColumn(name = "mongImageId")
    private MongImage mongImage;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;


    public static Mong create(String name, MongImage mongImage, User user) {
        return Mong.builder()
                .name(name)
                .lastGrowthDate(LocalDate.now())
                .mongImage(mongImage)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public void levelUp(MongImage nextImage) {
        if (this.level < 3) {
            this.level++;
            this.mongImage = nextImage;
            this.lastGrowthDate = LocalDate.now();
        }
    }

}
