package konkuk.kuit.durimong.domain.chatbot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotChattingReq {
    @Schema(description = "채팅봇 ID", example = "1")
    private Long chatBotId;

}
