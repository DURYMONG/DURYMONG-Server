package konkuk.kuit.durimong.domain.activity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "일별 활동 기록 조회")
public class ActivityDayRecordRes {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2024-11-28")
    private LocalDate date;

    @Schema(description = "유저 닉네임", example = "엘리자베스")
    private String nickname;

    @Schema(description = "몽 이름", example = "쿠쿠몽")
    private String mongName;

    @Schema(description = "몽 이미지", example = "url-to-mongImage")
    private String mongImage;

    @Schema(description = "채팅봇 이름", example = "바둑이")
    private String botName;
}
