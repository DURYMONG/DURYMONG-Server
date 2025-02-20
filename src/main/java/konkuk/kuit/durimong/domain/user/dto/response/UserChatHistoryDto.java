package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChatHistoryDto{
    @Schema(description = "대화 ID", example = "1")
    private Long conversationId;

    @Schema(description = "대화 날짜", example = "2025/01/01")
    private LocalDate createdAt;

    @Schema(description = "몽의 질문", example = "좋아하는 장소가 어딘가요?")
    private String mongQuestion;

    @Schema(description = "유저의 답변", example = "건국대학교")
    private String userAnswer;
}
