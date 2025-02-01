package konkuk.kuit.durimong.domain.activity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "활동/테스트 리스트 조회 ResponseDTO")
public class ActivityTestListRes {
    @Schema(description = "몽 이미지", example = "url-to-mongImage")
    private String mongImage;

    @Schema(description = "몽 이름", example = "쿠쿠몽")
    private String mongName;

    @Schema(description = "활동 리스트")
    private List<ActivityListDTO> activityList;

    @Schema(description = "테스트 리스트")
    private List<TestListDTO> testList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 활동 DTO")
    public static class ActivityListDTO {
        @Schema(description = "활동 이름", example = "침구 정리하기")
        private String activityName;

        @Schema(description = "체크 여부", example = "true")
        private boolean isChecked;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 활동 DTO")
    public static class TestListDTO {
        @Schema(description = "활동 id", example = "1")
        private Long testId;

        @Schema(description = "활동 이름", example = "침구 정리하기")
        private String testName;

    }


}
