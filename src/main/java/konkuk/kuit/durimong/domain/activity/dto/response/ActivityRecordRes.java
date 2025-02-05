package konkuk.kuit.durimong.domain.activity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "월별 활동 기록화면 ResponseDTO")
public class ActivityRecordRes {
    @Schema(description = "유저 닉네임", example = "엘리자베스")
    private String nickname;

    @Schema(description = "현재 월", example = "1")
    private int month;

    @Schema(description = "일별 활동 횟수 목록")
    private List<DayActivityCountDTO> dayCountList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 카테고리 DTO")
    public static class DayActivityCountDTO {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "날짜", example = "2025-01-19")
        private LocalDate date;

        @Schema(description = "실천 활동 개수", example = "2")
        private Integer count;

    }
}
