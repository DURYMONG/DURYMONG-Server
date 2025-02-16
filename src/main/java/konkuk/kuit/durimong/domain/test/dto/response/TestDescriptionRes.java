package konkuk.kuit.durimong.domain.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "테스트 설명 ResponseDTO")
public class TestDescriptionRes {
    @Schema(description = "테스트 id", example = "1")
    private Long testId;

    @Schema(description = "테스트 이름", example = "스트레스 수치 검사")
    private String testName;

    @Schema(description = "테스트 영문 이름", example = "BEPSI")
    private String testEnglishName;

    @Schema(description = "테스트 내용", example = "지난 한 달 동안의...")
    private String content;

    @Schema(description = "테스트 평가 정보", example = "'0 = 전혀 없었다' ~ '3 = 항상 있었다'까지 평가합니다.\n\n평가 결과가 20점 이상인 대상자는 전문가의 상담을 받는 것을 권유합니다.")
    private String evaluationInfo;

    @Schema(description = "문항 수", example = "11")
    private int countOfQuestions;

    @Schema(description = "소요시간", example = "5")
    private int requiredTime;

    @Schema(description = "직전 테스트 결과 정보")
    private LastTestDTO lastTestDTO;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "직전 테스트 결과 DTO")
    public static class LastTestDTO {
        @Schema(description = "직전 검사 날짜", example = "2025-02-06")
        private LocalDate date;

        @Schema(description = "유저 닉네임", example = "엘리자베스")
        private String userName;

        @Schema(description = "직전 테스트 점수", example = "15")
        private Integer lastScore;
    }
}
