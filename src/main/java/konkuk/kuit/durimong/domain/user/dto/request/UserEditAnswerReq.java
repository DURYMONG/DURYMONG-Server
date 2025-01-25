package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserEditAnswerReq {
    @Schema(description = "수정 답변", example = "아침에 샐러드를 먹었어.")
    private String newAnswer;
}
