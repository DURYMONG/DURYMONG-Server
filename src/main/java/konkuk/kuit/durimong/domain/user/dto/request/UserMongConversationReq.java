package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserMongConversationReq {
    @Schema(description = "유저의 대답", example = "오늘은 토스트를 먹었어")
    private String userAnswer;
}
