package konkuk.kuit.durimong.domain.user.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = User.UserBuilder.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Column
    private String FcmToken;

    public static User create(String id, String password, String email, String name, String nickname) {
        return User.builder()
                .id(id)
                .password(password)
                .email(email)
                .name(name)
                .nickname(nickname)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
    }
}
