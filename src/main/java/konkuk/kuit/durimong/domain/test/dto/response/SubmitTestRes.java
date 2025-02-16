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
    private String nickName;

    @Schema(description = "사용자의 테스트 점수", example = "15")
    private int userScore;

    @Schema(description = "사용자의 검사 결과")
    private String userResult;

    @Schema(description = "테스트 점수 분포 리스트")
    private String scoreDistributionList;
}
