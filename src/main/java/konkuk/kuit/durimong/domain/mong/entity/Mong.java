package konkuk.kuit.durimong.domain.mong.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.*;

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

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MongType mongType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MongColor color;

    @Builder.Default
    private int level = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;


    public static Mong create(String name, String type, String color, User user) {
        MongType mongType = MongType.from(type);
        MongColor mongColor = MongColor.from(color);
        return Mong.builder()
                .name(name)
                .image(mongType.getImagePath(mongColor,1))
                .color(mongColor)
                .mongType(mongType)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public void levelUp() {
        if (this.level < 3) {
            this.level++;
            this.image = this.mongType.getImagePath(this.color,this.level);
        }
    }

}
