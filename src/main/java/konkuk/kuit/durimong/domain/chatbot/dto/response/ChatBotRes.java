package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatBotRes {
    @Schema(description = "채팅봇 ID", example = "1")
    private Long chatBotId;

    @Schema(description = "채팅봇 이름", example = "바둑이")
    private String name;

    @Schema(description = "채팅봇 MBTI", example = "INTP")
    private String mbti;

    @Schema(description = "채팅봇 말투", example = "반말")
    private String accent;

    @Schema(description = "채팅봇 별명", example = "해결사")
    private String nickname;

    @Schema(description = "채팅봇 한줄 소개", example = "고민에 대한 해결을 원한다면 들어와 내가 해결할 수 있도록 도와줄게...!")
    private String slogan;

    @Schema(description = "채팅봇 이미지 url", example = "image.png")
    private String image;
}
