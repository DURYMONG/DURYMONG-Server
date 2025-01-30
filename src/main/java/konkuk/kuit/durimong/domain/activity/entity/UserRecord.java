package konkuk.kuit.durimong.domain.activity.entity;

import jakarta.persistence.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_record")
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_record_id")
    private Long userRecordId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
