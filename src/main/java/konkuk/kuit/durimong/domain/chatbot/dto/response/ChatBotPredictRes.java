package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotPredictRes {
    @Schema(description = "채팅봇의 대답")
    private String message;

    @Schema(description = "채팅봇 이미지", example = "image")
    private String chatBotImage;

    @Schema(description = "추천 칼럼 ID들")
    private List<Long> categoryIds;
}
