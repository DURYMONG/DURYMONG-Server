package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatBotChattingRes {
    @Schema(description = "채팅봇 이미지", example = "image")
    private String chatBotImage;

    @Schema(description = "채팅봇의 말", example = "엘리자베스야 안녕! 어떤 증상을 느꼈니?")
    private String message;


}
