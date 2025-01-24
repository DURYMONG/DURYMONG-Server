package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserHomeRes {
    @Schema(description = "오늘 날짜", example = "2025/01/01")
    private LocalDate date;

    @Schema(description = "몽과 함께한 시간", example = "16")
    private int withMongDate;

    @Schema(description = "몽 이름", example = "두리몽")
    private String mongName;

    @Schema(description = "몽 이미지")
    private String mongImage;

    @Schema(description = "몽 질문", example = "오늘 무엇을 먹었나요?")
    private String mongQuestion;

}
