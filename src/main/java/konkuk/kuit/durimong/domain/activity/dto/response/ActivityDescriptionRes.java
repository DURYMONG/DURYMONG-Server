package konkuk.kuit.durimong.domain.activity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "박스x 형식 활동 설명화면 ResponseDTO")
public class ActivityDescriptionRes {
    @Schema(description = "활동이름", example = "30분 걷기")
    private String activityName;

    @Schema(description = "인트로 설명", example = "좋아하는 노래와 함께 30분만 걸어볼까요?")
    private String intro;

    @Schema(description = "효과", example = "간단한 걷기 운동은 행복 호르몬을 분비하여 기분을 개선해주고, 심리적인 안정감을 주는데 도움이 됩니다.")
    private String effect;

    @Schema(description = "팁", example = "강가를 걷거나 주변 공원, 학교를 걸어도 좋아요!")
    private String tip;
}
