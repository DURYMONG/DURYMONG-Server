package konkuk.kuit.durimong.domain.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "테스트 검사 시작 ResponseDTO")
public class DoTestRes {
    @Schema(description = "테스트 id", example = "1")
    private Long testId;

    @Schema(description = "테스트 이름", example = "스트레스 수치 검사")
    private String testName;

    @Schema(description = "문항 수", example = "11")
    private int numberOfQuestions;

    @Schema(description = "선택지 개수", example = "3")
    private int numberOfOptions;

    @Schema(description = "질문 리스트")
    private List<QuestionListDTO> questionList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 활동 DTO")
    public static class QuestionListDTO {
        @Schema(description = "번호", example = "1")
        private int number;

        @Schema(description = "문항", example = "쉽게 짜증이 나고 기분의 변동이 심하다.")
        private String question;
    }
}
