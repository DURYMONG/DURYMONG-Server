package konkuk.kuit.durimong.domain.chatbot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestListDto{
    @Schema(description = "테스트 ID", example = "1")
    private Long testId;

    @Schema(description = "테스트 이름", example = "외상 후 스트레스 검사")
    private String testName;

}