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

    @Schema(description = "최소 선택지 번호", example = "1")
    private int minNumber;

    @Schema(description = "최소 선택지 문구", example = "그렇지 않다")
    private String  minOption;

    @Schema(description = "최대 선택지 번호", example = "3")
    private int maxNumber;

    @Schema(description = "최대 선택지 문구", example = "매우 그렇다")
    private String  maxOption;

    @Schema(description = "임계 점수", example = "22")
    private int criticalScore;

    @Schema(description = "문항 수 / 소요시간 리스트", example = "5 ~ 10문항 / 5 ~ 10분")
    private List<QuestionAndTimeListDTO> questionAndTimeList;

    @Schema(description = "직전 검사 기록")
    private List<LastTestListDTO> lastTestInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "문항 수 / 소요시간 DTO")
    public static class QuestionAndTimeListDTO {
        @Schema(description = "최소 문항 수", example = "11")
        private int minQuestion;

        @Schema(description = "최대 문항 수", example = "20 (nullable")
        private Integer maxQuestion;

        @Schema(description = "최소 소요시간", example = "3")
        private int minTime;

        @Schema(description = "최대 소요시간", example = "5 (nullable")
        private Integer maxTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "직전 테스트 결과 DTO")
    public static class LastTestListDTO {
        @Schema(description = "직전 검사 날짜", example = "2025-02-06")
        private LocalDate date;

        @Schema(description = "유저 닉네임", example = "엘리자베스")
        private String userName;

        @Schema(description = "직전 테스트 점수", example = "15")
        private int lastScore;
    }
}
