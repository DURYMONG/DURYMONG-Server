package konkuk.kuit.durimong.domain.mong.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonDeserialize(builder = Mong.MongBuilder.class)
public class Mong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MongId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String color;

    @Builder.Default
    private int level = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;


    public static Mong create(String name, String image, String color, User user) {
        return Mong.builder()
                .name(name)
                .image(image)
                .color(color)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
