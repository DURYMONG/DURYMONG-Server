package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDailyBotChatReq {
    @Schema(description = "날짜", example = "2025-02-13")
    private LocalDate targetDate;

    @Schema(description = "채팅봇 ID", example = "1")
    private Long chatBotId;
}
