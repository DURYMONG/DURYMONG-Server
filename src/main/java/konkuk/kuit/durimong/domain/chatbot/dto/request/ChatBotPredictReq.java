package konkuk.kuit.durimong.domain.chatbot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatBotPredictReq {
    @Schema(description = "채팅봇 ID", example = "1")
    private Long chatBotId;

    @Schema(description = "유저가 선택한 증상들")
    private List<String> symptoms;

    @Schema(description = "추가하기를 통해 추가 입력한 유저의 증상")
    private String additionalSymptoms;

    @Schema(description = "채팅 세션 ID", example = "1")
    private Long chatSessionId;
}
