package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaveChattingRes {
    @Schema(description = "채팅봇 메시지", example = "대화 즐거웠어! 엘리자베스를 항상 응원할게")
    private String botMessage;
}
