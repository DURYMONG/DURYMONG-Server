package konkuk.kuit.durimong.domain.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.kuit.durimong.domain.test.entity.ScoreDistribution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SubmitTestRes {
    @Schema(description = "테스트 이름", example = "스트레스 수치 검사")
    private String testName;

    @Schema(description = "사용자 이름", example = "엘리자베스")
    private String userName;

    @Schema(description = "사용자의 테스트 점수", example = "15")
    private int userScore;

    @Schema(description = "사용자의 스트레스 검사 결과")
    private UserResult userResult;

    @Schema(description = "점수 분포 정보 리스트")
    private List<ScoreDistributionInfo> scoreDistributionList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자의 스트레스 검사 결과 DTO")
    public static class UserResult {
        @Schema(description = "최소 점수", example = "14")
        private int minScore;

        @Schema(description = "최대 점수", example = "16(nullable)")
        private Integer maxScore;

        @Schema(description = "사용자의 스트레스 수준 설명", example = "스트레스 지수 40% 비교적 심한 스트레스")
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "스트레스 점수 분포 DTO")
    public static class ScoreDistributionInfo {
        @Schema(description = "최소 점수", example = "14")
        private int minScore;

        @Schema(description = "최대 점수", example = "16(nullable)")
        private Integer maxScore;

        @Schema(description = "해당 점수의 스트레스 설명", example = "스트레스 지수 40% (비교적 심한 스트레스)")
        private String description;
    }
}
