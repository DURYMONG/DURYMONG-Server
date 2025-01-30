package konkuk.kuit.durimong.domain.activity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "활동 체크 Response DTO")
public class CheckActivityRes {
    @Schema(description = "활동 id", example = "1")
    private Long activityId;

    @Schema(description = "유저 id", example = "3")
    private Long userId;

    @Schema(description = "체크 여부", example = "true")
    private boolean isChecked;

    // 년도-월-일 형식으로 (역)직렬화
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "오늘 날짜", example = "2024-11-28")
    private LocalDate date;

}
