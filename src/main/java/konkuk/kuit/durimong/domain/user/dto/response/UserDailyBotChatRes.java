package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDailyBotChatRes {
    @Schema(description = "날짜", example = "2월 13일의 상담")
    private String targetDate;

    @Schema(description = "채팅봇 이미지", example = "imageUrl")
    private String chatBotImage;

    private List<ChatHistoryDto> chatHistory;


    @AllArgsConstructor
    @Getter
    public static class ChatHistoryDto {
        @Schema(description = "대화 시작 시간", example = "12:27")
        private String startTime;

        @Schema(description = "채팅봇 인삿말")
        private String greetingMessage;

        @Schema(description = "유저가 선택한 증상")
        private List<String> symptoms;

        @Schema(description = "유저가 추가입력한 증상")
        private String addtionalSymptom;

        @Schema(description = "채팅봇 질환 추측/ 칼럼 및 활동 추천")
        private String botPredictionMessage;

        @Schema(description = "채팅봇 테스트 추천")
        private String testRecommendationMessage;

        @Schema(description = "추천 테스트 목록")
        private List<String> recommendedTests;

        @Schema(description = "채팅봇 일기 추천")
        private String diaryRecommendationMessage;

        @Schema(description = "채팅봇 마무리 인사말")
        private String finalMessage;
    }
}
