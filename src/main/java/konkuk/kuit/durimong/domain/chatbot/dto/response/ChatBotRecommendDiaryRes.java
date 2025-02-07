package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatBotRecommendDiaryRes {
    @Schema(description = "채팅봇 이미지", example = "image")
    private String chatBotImage;

    @Schema(description = "채팅봇 메시지", example = "오늘 하루는 어땠어? 하루 기록을 남겨봐")
    private String message;
}
