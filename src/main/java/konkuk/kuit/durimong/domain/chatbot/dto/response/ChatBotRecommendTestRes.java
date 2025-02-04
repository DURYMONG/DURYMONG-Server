package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ChatBotRecommendTestRes {
    @Schema(description = "유저 닉네임", example = "엘리자베스")
    private String nickname;

    @Schema(description = "채팅봇 이미지", example = "image")
    private String chatBotImage;

    @Schema(description = "테스트 목록")
    private List<TestListDto> testList;

    private static class TestListDto{
        @Schema(description = "테스트 ID", example = "1")
        private Long testId;

        @Schema(description = "테스트 이름", example = "외상 후 스트레스 검사")
        private String testName;

    }
}
