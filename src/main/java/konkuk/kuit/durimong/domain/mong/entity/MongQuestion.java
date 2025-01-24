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
public class MongQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mongQuestionId;

    @Schema(nullable = false)
    private String question;

}
