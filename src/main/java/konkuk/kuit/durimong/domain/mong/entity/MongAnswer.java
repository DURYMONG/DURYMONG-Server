package konkuk.kuit.durimong.domain.mong.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MongAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mongAnswerId;

    @Schema(nullable = false)
    private String question;

    @ManyToOne
    @JoinColumn(name = "MongId")
    private Mong mong;
}
