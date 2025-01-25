package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserDeleteChatReq {
    @Schema(description = "삭제할 대화의 기본 키", example = "1")
    private Long conversationId;
}
