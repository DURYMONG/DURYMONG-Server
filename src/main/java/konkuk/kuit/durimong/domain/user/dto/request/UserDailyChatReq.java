package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDailyChatReq {
    @Schema(description = "날짜")
    private LocalDate targetDate;
}
