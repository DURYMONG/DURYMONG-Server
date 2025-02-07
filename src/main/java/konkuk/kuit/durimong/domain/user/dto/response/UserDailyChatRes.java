package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserDailyChatRes {
    @Schema(description = "대화 날짜", example = "2025/01/01")
    private LocalDate createdAt;

    @Schema(description = "몽의 질문", example = "좋아하는 장소가 어딘가요?")
    private String mongQuestion;

    @Schema(description = "유저의 답변", example = "건국대학교")
    private String userAnswer;
}
