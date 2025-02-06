package konkuk.kuit.durimong.domain.test.entity;

import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_test")
public class UserTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_test_id")
    private Long userTestId;

    @Column(nullable = false)
    private int score;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
