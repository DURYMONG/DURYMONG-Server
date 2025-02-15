package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserDailyBotChatChoiceRes {
    @Schema(description = "날짜", example = "11월 28일의 기록")
    private String targetDate;

    @Schema(description = "채팅봇 대화 기록 정보")
    List<ChatBotChoiceDto> chatBotChoiceDtos;


    @Getter
    @AllArgsConstructor
    public static class ChatBotChoiceDto {
        @Schema(description = "채팅봇 ID",example = "1")
        private Long chatBotId;

        @Schema(description = "채팅봇 이미지")
        private String chatBotImage;

        @Schema(description = "(채팅봇 이름)와의 대화보기", example = "바둑이와의 대화 보기")
        private String description;


    }
}
